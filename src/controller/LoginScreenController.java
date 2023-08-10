package controller;

import dao.AppointmentQuery;
import dao.UserQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class that gives control logic for login screen window of application
 */
public class LoginScreenController implements Initializable {

    /**
     * Exit button of login screen
     */
    @FXML
    public Button loginExitBtn;

    /**
     * Login button of login screen
     */
    @FXML
    private Button loginBtn;

    /**
     * Username label on login screen
     */
    @FXML
    private Label loginUsernameLbl;

    /**
     * Password label on login screen
     */
    @FXML
    private Label loginPasswordlbl;

    /**
     * Locale label on login screen
     */
    @FXML
    private Label localeLbl;

    /**
     * Entry field for username
     */
    @FXML
    private TextField userNameEntryTxt;

    /**
     * Entry field for user password
     */
    @FXML
    private TextField passwordEntryTxt;

    Stage stage;
    Parent scene;


    /**
     * Method to quickly implement alert with of type confirmation
     *
     * @param title title of alert
     * @param content content of alert
     * @return true if OK button selected, else false
     */
    static boolean confirmBox(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }




    /**
     * Method to check if username and password are correct, alert user with information about appointments, and switch
     * locales depending on system default locale
     *
     * @param event login button action
     * @throws SQLException mysql
     * @throws IOException FXMLLoader
     */
    public void onActionLogin(ActionEvent event) throws SQLException, IOException {

        try{

            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
            LocalDateTime nowMinus15Min = LocalDateTime.now().minusMinutes(15);
            LocalDateTime nowPlus15Min = LocalDateTime.now().plusMinutes(15);
            LocalDateTime apptStartTime;
            int appointmentID = 0;
            LocalDateTime showTime = null;
            boolean appointmentIn15Min = false;



        String userName = String.valueOf(userNameEntryTxt.getText());
        String password = String.valueOf(passwordEntryTxt.getText());





        FileWriter writeToFile = new FileWriter("login_activity.txt", true);

        PrintWriter loginActivityFile = new PrintWriter(writeToFile);


        if (UserQuery.loginCheck(userName, password)) {

            loginActivityFile.println("user: " + userName + " logged in successfully at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

            loginActivityFile.close();


        }

        if(UserQuery.loginCheck(userName, password)){

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));

            stage.setScene(new Scene(scene));
            stage.show();

            for (Appointment appointment: allAppointments) {
                apptStartTime = appointment.getApptStart();

                if ((apptStartTime.isAfter(nowMinus15Min) || apptStartTime.isEqual(nowMinus15Min)) && (apptStartTime.isBefore(nowPlus15Min) || (apptStartTime.isEqual(nowPlus15Min)))) {
                    appointmentID = appointment.getApptId();
                    showTime = apptStartTime;
                    appointmentIn15Min = true;
                }
            }

            if (appointmentIn15Min) {

                MainMenuController.alertBox("Upcoming Appointment!", "You have an upcoming appointment with ID of " + appointmentID + " starting at: " + showTime, Alert.AlertType.INFORMATION);
            }
            else {

                MainMenuController.alertBox("No Pending Appointments", "You have no pending appointments", Alert.AlertType.INFORMATION);
            }

        }
        else if(!(UserQuery.loginCheck(userName, password))) {


            loginActivityFile.print("user: " + userName + " was unsuccessful at logging in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
             loginActivityFile.close();


            Alert alert = new Alert(Alert.AlertType.ERROR);

            if (Locale.getDefault().getLanguage().equals("fr")) {
                alert.setTitle("Entrée Encorrecte");
                alert.setContentText("Identifiant ou mot de passe incorrect");
                alert.showAndWait();
            } else {
                alert.setTitle("Incorrect Input");
                alert.setContentText("Incorrect username or password");
                alert.showAndWait();
            }
        }
    } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to exit program and confirm that user wants to exit program. Switches
     * from English to French depending on locale.
     *
     * @param actionEvent  login screen exit button action
     */
        public void onActionExitProgram(ActionEvent actionEvent) {
        if (Locale.getDefault().getLanguage().equals("fr")) {
            confirmBox("Sortie du système", "Êtes-vous sûr de vouloir quitter?");
        }
        else {
            confirmBox("System Exit", "Are you sure you want to exit?");
        }

        System.exit(0);

    }


    /**
     * Controller initializes to set labels and alerts to English or French depending
     * on system default locale
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Locale france = new Locale("fr", "FR");
        Locale english = new Locale("en", "US");

        ZoneId userZone = ZoneId.systemDefault();

        //Locale.setDefault();


        ResourceBundle rb = ResourceBundle.getBundle("/resources/Login", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")){
            loginUsernameLbl.setText(rb.getString("username"));
            loginPasswordlbl.setText(rb.getString("password"));
            localeLbl.setText(String.valueOf(userZone));
            loginBtn.setText(rb.getString("login"));
            loginExitBtn.setText(rb.getString("exit"));

        }
        else{
            loginUsernameLbl.setText(rb.getString("username"));
            loginPasswordlbl.setText(rb.getString("password"));
            localeLbl.setText(String.valueOf(userZone));
            loginBtn.setText(rb.getString("login"));
            loginExitBtn.setText(rb.getString("exit"));
        }

    }


}
