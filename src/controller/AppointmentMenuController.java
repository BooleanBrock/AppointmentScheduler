package controller;

import dao.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainMenuController.alertBox;

/**
 * Controller class that provides control logic for Appointment Menu window within application
 *
 * @author Wyatt Brock
 */
public class AppointmentMenuController implements Initializable {

    /**
     * Appointment tableview
     */
    @FXML
    private TableView apptTableView;

    /**
     * Appointment ID column of table view
     */
   @FXML
   private TableColumn apptIDCol;

    /**
     * Appointment title column of table view
     */
   @FXML
   private TableColumn apptTitleCol;

    /**
     * Appointment description column of table view
     */
   @FXML
   private TableColumn apptDescriptionCol;

    /**
     * Appointment location column of table view
     */
   @FXML
   private TableColumn apptLocationCol;

    /**
     * Appointment contact column of table view
     */
   @FXML
   private TableColumn apptContactCol;

    /**
     * Appointment type column of table view
     */
   @FXML
   private TableColumn apptTypeCol;

    /**
     * Appointment start date and time column of table view
     */
   @FXML
   private TableColumn apptStartCol;

    /**
     * Appointment end date and time column of table view
     */
   @FXML
   private TableColumn apptEndCol;

    /**
     * Appointment customer column of table view
     */
   @FXML
   private TableColumn apptCustomerCol;

    /**
     * Appointment user column of table view
     */
   @FXML
   private TableColumn apptUserCol;

    /**
     * Toggle group for appointment radio buttons
     */
   @FXML
   ToggleGroup apptFilterRadBtn;

    /**
     * Radio button to display all appointments
     */
   @FXML
   private RadioButton AllFilterRadioBtn;

    /**
     * Radio button to display appointments by week
     */
   @FXML
   private RadioButton WeeksFilterRadBtn;

    /**
     * Radio button to display appointments by month
     */
   @FXML
   private RadioButton MonthsFilterRadBtn;

    Stage stage;

    Parent scene;


    /**
     * Method to switch to Add Appointment Menu
     *
     * @param event on action loads add appointment menu
     * @throws IOException FXMLLoader
     */
    public void onActionAddAppt(ActionEvent event) throws IOException {
        MainMenuController.sceneLoader(event, "/view/AddAppointmentMenu.fxml");

    }

    /**
     * Method to display all appointments in table view
     *
     * @param event on action displays all appointments
     * @throws SQLException mysql
     */
    public void onActionDisplayAllAppts(ActionEvent event) throws SQLException {

        try{

        if(AllFilterRadioBtn.isSelected()){

            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();

            allAppointments.stream().sorted();

            if(allAppointments != null){

                apptTableView.setItems(allAppointments);
            }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to filter appointments by week
     *
     * lambda used to compare start date time and end date time of each appointment object
     *
     * @param event on action, filters appointments by week
     * @throws SQLException mysql
     */
    public void onActionFilterByWeeks(ActionEvent event) throws SQLException {

       try {

           if (WeeksFilterRadBtn.isSelected()) {

               ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
               ObservableList<Appointment> allAppointmentWeeks = FXCollections.observableArrayList();

               LocalDateTime startOfCurrentWeek = LocalDateTime.now().minusWeeks(1);
               LocalDateTime endOfCurrentWeek = LocalDateTime.now().plusWeeks(1);

               if (allAppointments != null) {

                   allAppointments.forEach(appointment -> {
                       if (appointment.getApptEnd().isAfter(startOfCurrentWeek) && appointment.getApptEnd().isBefore(endOfCurrentWeek)) {

                           allAppointmentWeeks.add(appointment);
                           allAppointmentWeeks.stream().sorted();
                       }

                       apptTableView.setItems(allAppointmentWeeks);

                   });
               }
           }
       }
       catch (SQLException e){
           throw new RuntimeException(e);
       }
    }


    /**
     * Method to filter appointments by months
     *
     * lambda used to compare start date time and end date time of each appointment object
     *
     * @param event on action filter appointments by months
     * @throws SQLException mysql
     */
    public void onActionFilterByMonths(ActionEvent event) throws SQLException {

        try {

            if(MonthsFilterRadBtn.isSelected()) {

                ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
                ObservableList<Appointment> allAppointmentMonths = FXCollections.observableArrayList();


                LocalDateTime startOfCurrentMonth = LocalDateTime.now().minusMonths(1);
                LocalDateTime endOfCurrentMonth = LocalDateTime.now().plusMonths(1);

                if (allAppointments != null) {

                    allAppointments.forEach(appointment -> {
                        if (appointment.getApptEnd().isAfter(startOfCurrentMonth) && appointment.getApptEnd().isBefore(endOfCurrentMonth)) {

                            allAppointmentMonths.add(appointment);

                        }

                        apptTableView.setItems(allAppointmentMonths);

                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to go to update appointment window
     *
     * @param event on action loads update appointment window and sends appointment info to new window
     * @throws IOException FXMLLoader
     */
    public void onActionUpdateAppt(ActionEvent event) throws IOException {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateAppointmentMenu.fxml"));
            loader.load();

            UpdateAppointmentMenuController UpApptController = loader.getController();
            UpApptController.sendAppointment(apptTableView.getSelectionModel().getSelectedIndex(), (Appointment) apptTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }

        catch (NullPointerException e) {
            MainMenuController.alertBox("No Appointment Selected", "Please select an appointment to update", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete appointment from tableview and database
     *
     * @param actionEvent on action removes appointment from tableview and database
     * @throws SQLException mysql
     */
    public void onActionDeleteAppt(ActionEvent actionEvent) throws SQLException {
        if (apptTableView.getSelectionModel().isEmpty()) {
            alertBox("Error: No appointment selected", "Please select an appointment to remove.", Alert.AlertType.CONFIRMATION);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Action");
            alert.setContentText("Are you sure you want to remove this appointment?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Appointment selectedAppointment = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
                int selectedApptID = selectedAppointment.getApptId();
                alertBox("Appointment deleted", "Selected appointment: " + selectedApptID + ", " + selectedAppointment.getApptType() + ", " + " was successfully deleted!", Alert.AlertType.INFORMATION);
                AppointmentQuery.delete(selectedApptID);


                apptTableView.setItems(AppointmentQuery.getAllAppointments());
            }
        }
    }

    /**
     * Method to return to Main Menu window
     *
     * @param event on action loads Main Menu window
     * @throws IOException FXMLLoader
     */
    public void onActionReturnToMainMenu(ActionEvent event) throws IOException {

        MainMenuController.sceneLoader(event, "/view/MainMenu.fxml");

    }


    /**
     * Controller initializes to add all appointment info to corresponding columns of tableview
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();


            apptTableView.setItems(allAppointments);


            apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
            apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));

            apptContactCol.setCellValueFactory(new PropertyValueFactory<>("apptContactName"));
            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
            apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
            apptUserCol.setCellValueFactory(new PropertyValueFactory<>("apptUserId"));




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}
