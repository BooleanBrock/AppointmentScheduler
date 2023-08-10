package controller;

import dao.AppointmentQuery;
import dao.ContactQuery;
import dao.CustomerQuery;
import dao.UserQuery;
import helper.TimeConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static controller.MainMenuController.alertBox;
import static controller.MainMenuController.sceneLoader;

/**
 * Controller class that gives control logic for add appointment window within application
 *
 * @author Wyatt Brock
 */
public class AddAppointmentMenuController implements Initializable {


    /**
     * Application Start Date Datepicker
     */
    @FXML
    private DatePicker apptStartDatePick;

    /**
     * Appointment End Date Datepicker
     */
    @FXML
    private DatePicker apptEndDatePick;

    /**
     * Appointment start time text field
     */
    @FXML
    private TextField apptStartTimeTxt;

    /**
     * Appointment end time text field
     */
    @FXML
    private TextField apptEndTimeTxt;

    /**
     * Appointment ID text field
     */
    @FXML
    private TextField addApptIDTxt;

    /**
     * Appointment title text field
     */
    @FXML
    private TextField addApptTitleTxt;

    /**
     * Appointment description text field
     */
    @FXML
    private TextField addApptDescTxt;

    /**
     * Appointment location text field
     */
    @FXML
    private TextField addApptLocTxt;

    /**
     * Appointment type text field
     */
    @FXML
    private TextField addApptTypeTxt;

    /**
     * Appointment customer ID text field
     */
    @FXML
    private TextField addApptCustIDTxt;

    /**
     * Appointment user ID text field
     */
    @FXML
    private TextField addApptUserIDTxt;

    /**
     * Appointment contact combo box
     */
    @FXML
    private ComboBox apptContactComboBox;

    /**
     * Method to generate the appointment ID
     *
     * lambda expression used to get all appointment ID from each appointment object
     *
     * @return int appointment ID
     * @throws SQLException mysql
     */
    public int getApptID() throws SQLException {

        ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
        ObservableList<Integer> allAppointmentIDs = FXCollections.observableArrayList();

        //lambda
        allAppointments.forEach(appointment -> allAppointmentIDs.add(appointment.getApptId()));

        int apptID;

        if (allAppointmentIDs.size() != 0) {
            apptID = allAppointmentIDs.get(allAppointmentIDs.size() - 1) + 1 ;
        } else {
            apptID = 1;
        }
        return apptID;
    }


    /**
     * Method to allow for saving a new appointment
     *
     * lambda to gather appointment ID into list from all appointment objects
     *
     * @param event action when save button is clicked
     * @throws SQLException mysql
     * @throws IOException FXMLLoader
     */
    public void onActionSaveAppt(ActionEvent event) throws SQLException, IOException {

        try {

            //check for empty fields

            if (addApptTitleTxt.getText().isEmpty() || addApptDescTxt.getText().isEmpty() || addApptLocTxt.getText().isEmpty() ||
                    addApptTypeTxt.getText().isEmpty() || apptStartDatePick.getValue() == null || apptEndDatePick.getValue() == null ||
                    apptStartTimeTxt.getText().isEmpty() || apptEndTimeTxt.getText().isEmpty() || addApptCustIDTxt.getText().isEmpty() ||
                    addApptUserIDTxt.getText().isEmpty() || apptContactComboBox.getSelectionModel().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No input");
                alert.setContentText("Please input value into all fields");
                alert.showAndWait();

            } else {


                ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
                ObservableList<Integer> allAppointmentIDs = FXCollections.observableArrayList();


                //format dates and times
                DateTimeFormatter minHourFormatter = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


                //lambda
                allAppointments.forEach(appointment -> allAppointmentIDs.add(appointment.getApptId()));


                int apptID = getApptID();
                String apptTitle = addApptTitleTxt.getText();
                String apptDesc = addApptDescTxt.getText();
                String apptLocation = addApptLocTxt.getText();
                String apptType = addApptTypeTxt.getText();
                int apptCustomerID = Integer.parseInt(addApptCustIDTxt.getText());
                int apptUserID = Integer.parseInt(addApptUserIDTxt.getText());

                String contactName = (String) apptContactComboBox.getSelectionModel().getSelectedItem();
                int apptContactID = ContactQuery.getContactID(contactName);



                LocalDate localApptStartDate = LocalDate.parse(apptStartDatePick.getValue().format(dateFormatter));
                LocalTime localApptStartTime = LocalTime.parse(apptStartTimeTxt.getText(), minHourFormatter);

                LocalDate localApptEndDate = LocalDate.parse(apptEndDatePick.getValue().format(dateFormatter));
                LocalTime localApptEndTime = LocalTime.parse(apptEndTimeTxt.getText(), minHourFormatter);


                //Local time
                LocalDateTime apptStart = LocalDateTime.of(localApptStartDate, localApptStartTime);
                LocalDateTime apptEnd = LocalDateTime.of(localApptEndDate, localApptEndTime);

                ZonedDateTime zonedApptStart = ZonedDateTime.of(apptStart, ZoneId.systemDefault());
                ZonedDateTime zonedApptEnd = ZonedDateTime.of(apptEnd, ZoneId.systemDefault());

                //Time in EST
                ZonedDateTime apptStartEST = zonedApptStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime apptEndEST = zonedApptEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

                //Time in UTC
                LocalDateTime apptStartUTC = TimeConverter.convertToUTC(apptStart);
                LocalDateTime apptEndUTC = TimeConverter.convertToUTC(apptEnd);


                //Start and end of business day
                LocalTime startOfDay = LocalTime.of(8, 0, 0);
                LocalTime endOfDay = LocalTime.of(22, 00, 00);

                //Start and end of business week
                int startOfWeek = DayOfWeek.MONDAY.getValue();
                int endOfWeek = DayOfWeek.FRIDAY.getValue();

                //Catch any time or day disagreements

             /*   if (apptStartEST.getDayOfWeek().getValue() < startOfWeek || apptStartEST.getDayOfWeek().getValue() > endOfWeek || apptEndEST.getDayOfWeek().getValue() < startOfWeek
                        || apptEndEST.getDayOfWeek().getValue() > endOfWeek) {

                    alertBox("Appointment Outside Business Days", "Appointments cannot be scheduled on Saturdays or Sundays", Alert.AlertType.WARNING);
                    return;

                }

              */

                if (apptStartEST.toLocalTime().isBefore(startOfDay) || apptStartEST.toLocalTime().isAfter(endOfDay)
                        || apptEndEST.toLocalTime().isBefore(startOfDay) || apptEndEST.toLocalTime().isAfter(endOfDay)) {

                    alertBox("Appointment Time Outside Business Hours", "Appointments cannot be scheduled outside of business hours between 8 AM EST and 10 PM EST", Alert.AlertType.WARNING);
                    return;
                }

                if (apptStart.isEqual(apptEnd)) {

                    alertBox("Appointment Error", "Appointment starts and ends at the same time", Alert.AlertType.WARNING);
                    return;

                }

                if (!UserQuery.getAllUserIDs().contains(apptUserID)) {

                    alertBox("No User Found", "No user with this ID exists", Alert.AlertType.WARNING);
                    return;

                }

                if (!CustomerQuery.getAllCustomerIDs().contains(apptCustomerID)) {

                    alertBox("No Customer Found", "No customer with this ID exists", Alert.AlertType.WARNING);
                    return;

                }

                if (localApptStartTime.isAfter(localApptEndTime)) {

                    alertBox("Times Do Not Agree", "The start time cannot be later than the end time.", Alert.AlertType.WARNING);
                    return;

                }

                if (localApptStartDate.isAfter(localApptEndDate)) {

                    alertBox("Dates Do Not Agree", "The start date cannot be later than the end date.", Alert.AlertType.WARNING);
                    return;

                }

                for (Appointment appointment : allAppointments) {

                    LocalDateTime startVerify = appointment.getApptStart();
                    LocalDateTime endVerify = appointment.getApptEnd();

                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptStart.isBefore(startVerify)) && (apptEnd.isAfter(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "An appointment already exists for this period of time", Alert.AlertType.ERROR);

                        return;
                    }
                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptStart.isEqual(startVerify)) && (apptEnd.isEqual(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                        return;
                    }
                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptStart.isEqual(startVerify)) && (apptEnd.isAfter(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                        return;
                    }
                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptStart.isBefore(startVerify)) && (apptEnd.isEqual(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                        return;
                    }

                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptStart.isAfter(startVerify)) && (apptStart.isBefore(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                        return;
                    }
                    if ((apptCustomerID == appointment.getApptCustomerID()) && (apptID != appointment.getApptId())
                            && (apptEnd.isAfter(startVerify)) && (apptEnd.isBefore(endVerify))) {

                        alertBox("Appointment Could Not Be Created", "Appointment end time is during the time of another", Alert.AlertType.ERROR);
                        return;
                    }
                }

                AppointmentQuery.insert(apptID, apptTitle, apptDesc, apptLocation, apptType, apptStartUTC, apptEndUTC, apptCustomerID, apptUserID, apptContactID);

                sceneLoader(event, "/view/AppointmentMenu.fxml");


            }

        } catch (SQLException | NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Return to Application Menu on click
     *
     * @param event Return to Appointment Menu action
     * @throws IOException FXMLLoader
     */
    public void onActionReturnToApptMenu(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/AppointmentMenu.fxml");

    }

    /**
     * Controller initializes to display all contacts names in combo box
     *
     * lambda used to gather all contact names into list from all contact objects
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<Contact> allContacts = ContactQuery.getAllContacts();
            ObservableList<String> allContactNames = FXCollections.observableArrayList();

            //lambda #1
            allContacts.forEach(contact -> allContactNames.add(contact.getContactName()));

            apptContactComboBox.setItems(allContactNames);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
