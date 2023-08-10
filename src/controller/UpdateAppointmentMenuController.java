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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static controller.MainMenuController.alertBox;
import static controller.MainMenuController.sceneLoader;

/**
 * Controller class that gives control logic for update appointment window within application
 *
 * @author Wyatt Brock
 */
public class UpdateAppointmentMenuController {
    /**
     * ID of appointment
     */
    @FXML
    private TextField newApptIDTxt;

    /**
     * Title of appointment
     */
    @FXML
    private TextField newApptTitleTxt;

    /**
     * Description of appointment
     */
    @FXML
    private TextField newApptDescTxt;

    /**
     * Location of appointment
     */
    @FXML
    private TextField newApptLocTxt;

    /**
     * Type of appointment
     */
    @FXML
    private TextField newApptTypeTxt;

    /**
     * Customer ID of appointment
     */
    @FXML
    private TextField newApptCustIDTxt;

    /**
     * User ID of appointment
     */
    @FXML
    private TextField newApptUserIDTxt;

    /**
     * Datepicker for start date
     */
    @FXML
    private DatePicker newStartDatePick;

    /**
     * Datepicker for end date
     */
    @FXML
    private DatePicker newEndDatePick;

    /**
     * Start time of appointment
     */
    @FXML
    private TextField newStartTimeTxt;

    /**
     * End time of appointment
     */
    @FXML
    private TextField newEndTimeTxt;

    /**
     * Combo box for selecting contact
     */
    @FXML
    private ComboBox newContactComboBox;

    private int currentlySelectedAppt = 0;

    /**
     * Method to load appointment to Updated Appointment Menu of application
     *
     * @param selectedIndex chosen from Appointment menu
     * @param selectedAppointment chosen from Appointment menu
     */
    public void sendAppointment(int selectedIndex, Appointment selectedAppointment) {

        int currentlySelectedAppt = selectedIndex;

        try {
            ObservableList<Contact> allContacts = ContactQuery.getAllContacts();
            ObservableList<String> allContactNames = FXCollections.observableArrayList();


            allContacts.forEach(contact -> allContactNames.add(contact.getContactName()));

            newContactComboBox.setItems(allContactNames);

            newContactComboBox.getSelectionModel().select(selectedAppointment.getApptContactId() - 1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        newApptIDTxt.setText(String.valueOf(selectedAppointment.getApptId()));
        newApptTitleTxt.setText(selectedAppointment.getApptTitle());
        newApptDescTxt.setText(selectedAppointment.getApptDescription());
        newApptLocTxt.setText(selectedAppointment.getApptLocation());
        newApptTypeTxt.setText(selectedAppointment.getApptType());
        newApptCustIDTxt.setText(String.valueOf(selectedAppointment.getApptCustomerID()));
        newApptUserIDTxt.setText(String.valueOf(selectedAppointment.getApptUserId()));
        newStartDatePick.setValue(selectedAppointment.getApptStart().toLocalDate());
        newStartTimeTxt.setText(String.valueOf(selectedAppointment.getApptStart().toLocalTime()));
        newEndDatePick.setValue(selectedAppointment.getApptEnd().toLocalDate());
        newEndTimeTxt.setText(String.valueOf(selectedAppointment.getApptEnd().toLocalTime()));

    }

    /**
     * Method to save and update new appointment info in database and tables,
     * throws alerts for time and date constraints and verifies correct entry of info
     *
     * @param event save button action
     * @throws SQLException mysql
     * @throws IOException FXMLLoader
     */
    public void onActionSaveNewAppt(ActionEvent event) throws SQLException, IOException {


        if (newApptTitleTxt.getText().isEmpty() || newApptDescTxt.getText().isEmpty() || newApptLocTxt.getText().isEmpty() ||
                newApptTypeTxt.getText().isEmpty() || newStartDatePick.getValue() == null || newEndDatePick.getValue() == null ||
                newStartTimeTxt.getText().isEmpty() || newEndTimeTxt.getText().isEmpty() || newApptCustIDTxt.getText().isEmpty() ||
                newApptUserIDTxt.getText().isEmpty() || newContactComboBox.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No input");
            alert.setContentText("Please input value into all fields");
            alert.showAndWait();
        } else {

            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();

            DateTimeFormatter minHourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



            int updatedApptID = Integer.parseInt(newApptIDTxt.getText());

            String updatedApptTitle = newApptTitleTxt.getText();
            String updatedApptDesc = newApptDescTxt.getText();
            String updatedApptLocation = newApptLocTxt.getText();
            String updatedApptType = newApptTypeTxt.getText();
            int updatedApptCustomerID = Integer.parseInt(newApptCustIDTxt.getText());
            int updatedApptUserID = Integer.parseInt(newApptUserIDTxt.getText());

            String contactName = (String) newContactComboBox.getSelectionModel().getSelectedItem();

            int updatedApptContactID = ContactQuery.getContactID(contactName);


            LocalDate localApptStartDate = LocalDate.parse(newStartDatePick.getValue().format(dateFormatter));
            LocalTime localApptStartTime = LocalTime.parse(newStartTimeTxt.getText(), minHourFormatter);

            LocalDate localApptEndDate = LocalDate.parse(newEndDatePick.getValue().format(dateFormatter));
            LocalTime localApptEndTime = LocalTime.parse(newEndTimeTxt.getText(), minHourFormatter);


            LocalDateTime apptStart = LocalDateTime.of(localApptStartDate, localApptStartTime);
            LocalDateTime apptEnd = LocalDateTime.of(localApptEndDate, localApptEndTime);


            ZonedDateTime zonedApptStart = ZonedDateTime.of(apptStart, ZoneId.systemDefault());
            ZonedDateTime zonedApptEnd = ZonedDateTime.of(apptEnd, ZoneId.systemDefault());

            ZonedDateTime apptStartEST = zonedApptStart.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime apptEndEST = zonedApptEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

            LocalDateTime apptStartUTC = TimeConverter.convertToUTC(apptStart);
            LocalDateTime apptEndUTC = TimeConverter.convertToUTC(apptEnd);


            LocalTime startOfDay = LocalTime.of(8, 0, 0);
            LocalTime endOfDay = LocalTime.of(22, 00, 00);

            int startOfWeek = DayOfWeek.MONDAY.getValue();
            int endOfWeek = DayOfWeek.FRIDAY.getValue();


            if (apptStartEST.getDayOfWeek().getValue() < startOfWeek || apptStartEST.getDayOfWeek().getValue() > endOfWeek || apptEndEST.getDayOfWeek().getValue() < startOfWeek
                    || apptEndEST.getDayOfWeek().getValue() > endOfWeek) {

                alertBox("Appointment Outside Business Days", "Appointments cannot be scheduled on Saturdays or Sundays", Alert.AlertType.WARNING);
                return;

            }

            if (apptStartEST.toLocalTime().isBefore(startOfDay) || apptStartEST.toLocalTime().isAfter(endOfDay)
                    || apptEndEST.toLocalTime().isBefore(startOfDay) || apptEndEST.toLocalTime().isAfter(endOfDay)) {

                alertBox("Appointment Time Outside Business Hours", "Appointments cannot be scheduled outside of business hours between 8 AM EST and 10 PM EST", Alert.AlertType.WARNING);
                return;
            }


            if (apptStart.isEqual(apptEnd)) {

                alertBox("Appointment Error", "Appointment starts and ends at the same time", Alert.AlertType.WARNING);
                return;

            }

            if (!UserQuery.getAllUserIDs().contains(updatedApptID)) {

                alertBox("No User Found", "No user with this ID exists", Alert.AlertType.WARNING);
                return;

            }

            if (!CustomerQuery.getAllCustomerIDs().contains(updatedApptCustomerID)) {

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

                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptStart.isBefore(startVerify)) && (apptEnd.isAfter(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "An appointment already exists for this period of time", Alert.AlertType.ERROR);

                    return;
                }
                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptStart.isEqual(startVerify)) && (apptEnd.isEqual(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                    return;
                }

                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptStart.isAfter(startVerify)) && (apptStart.isBefore(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                    return;
                }
                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptStart.isEqual(startVerify)) && (apptEnd.isAfter(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                    return;
                }
                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptStart.isBefore(startVerify)) && (apptEnd.isEqual(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "Appointment start time is during time of another", Alert.AlertType.ERROR);
                    return;
                }
                if ((updatedApptCustomerID == appointment.getApptCustomerID()) && (updatedApptID != appointment.getApptId())
                        && (apptEnd.isAfter(startVerify)) && (apptEnd.isBefore(endVerify))) {

                    alertBox("Appointment Could Not Be Created", "Appointment end time is during the time of another", Alert.AlertType.ERROR);
                    return;
                }
            }

            AppointmentQuery.update(updatedApptID, updatedApptTitle, updatedApptDesc, updatedApptLocation, updatedApptType, apptStartUTC, apptEndUTC, updatedApptCustomerID, updatedApptUserID, updatedApptContactID);

            sceneLoader(event, "/view/AppointmentMenu.fxml");

        }
    }

    /**
     * Method to return Appointment Menu window of application
     *
     * @param event cancel button click
     * @throws IOException FXMLLoader
     */
        public void onActionReturnToApptMenu (ActionEvent event) throws IOException {

            MainMenuController.sceneLoader(event, "/view/AppointmentMenu.fxml");

        }
    }

