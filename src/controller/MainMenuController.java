package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class that gives control logic for Main Menu window of application
 *
 * @author Wyatt Brock
 */
public class MainMenuController {

    static Stage stage;

    static Parent scene;

    /**
     * Method to quickly create alert
     *
     * @param title title of alert
     * @param content content of alert
     * @param type type of alert
     * @return true if OK button pressed, otherwise return false
     */
    public static Boolean alertBox(String title, String content, Alert.AlertType type){

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }

    }


    /**
     * Method to quickly load scene
     *
     * @param event button clicked
     * @param source provided source in code
     * @throws IOException FXMLLoader
     */
    public static void sceneLoader(ActionEvent event, String source) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();

        scene = FXMLLoader.load(MainMenuController.class.getResource(source));

        stage.setScene(new Scene(scene));

        stage.show();
    }

    /**
     * Method to load screen to Customer Menu window of application
     *
     * @param event Customers button click
     * @throws IOException FXMLLoader
     */
    public void onActionDisplayCustomers(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/CustomerMenu.fxml");

    }

    /**
     * Method to load Appointment Menu window of application
     *
     * @param event Appointments button click
     * @throws IOException FXMLLoader
     */
    public void onActionDisplayAppointments(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/AppointmentMenu.fxml");
    }

    /**
     * Method to load Reports Menu window of application
     *
     * @param event Reports button click
     * @throws IOException FXMLLoader
     */
    public void onActionDisplayReports(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/ReportMenu.fxml");
    }

    /**
     * Method to exit application
     *
     * @param event Exit button click
     */
    public void OnActionExitApp(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setContentText("Are you sure you want to exit the application?");
        alert.showAndWait();

        System.exit(0);
    }
}
