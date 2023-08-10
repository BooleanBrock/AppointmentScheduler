package controller;

import dao.AppointmentQuery;
import dao.CustomerQuery;
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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainMenuController.alertBox;

/**
 * Controller class that gives control logic for Customer Menu window within application
 *
 * @author Wyatt Brock
 */
public class CustomerMenuController implements Initializable {

    /**
     * Country of the customer
     */
    @FXML
    private TableColumn<Customer, String> customerCountryCol;

    /**
     * Searchbox to find customer by name or ID
     */
    @FXML
    private TextField customerSearchBox;

    /**
     * Customer tableview that displays all customers
     */
    @FXML
    private TableView<Customer> customerTableView;

    /**
     * Customer ID of the customer
     */
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;

    /**
     * Name of customer
     */
    @FXML
    private TableColumn<Customer, String> customerNameCol;

    /**
     * Address of customer
     */
    @FXML
    private TableColumn<Customer, String> customerAddressCol;

    /**
     * Postal code of customer
     */
    @FXML
    private TableColumn<Customer, String> customerZipCol;

    /**
     * Phone number of customer
     */
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;

    /**
     * Division ID of customer
     */
    @FXML
    private TableColumn<Customer, Integer> customerDivIDCol;

    /**
     * Division of customer
     */
    @FXML
    private TableColumn<Customer, String> customerDivCol;


    Stage stage;

    Parent scene;

    /**
     * Method to allow for quick setting and loading of scenes
     *
     * @param event action when button is clicked
     * @param source Given source for desired scene
     * @throws IOException FXMLLoader
     */
    public void sceneLoader(ActionEvent event, String source) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();

        scene = FXMLLoader.load(getClass().getResource(source));

        stage.setScene(new Scene(scene));

        stage.show();
    }

    /**
     * Search for customer in searchbox by name or ID
     *
     * @param actionEvent Search for customer on entry
     * @throws SQLException mysql
     */
    public void onActionSearchCustomer(ActionEvent actionEvent) throws SQLException {

        String userEntry = customerSearchBox.getText();

        ObservableList<Customer> foundCustomers = CustomerQuery.lookupCustomer(userEntry);

        try {
            while(foundCustomers.size() == 0) {

                int foundCustomerId = Integer.parseInt(userEntry);
                foundCustomers.add(CustomerQuery.lookupCustomer(foundCustomerId));
            }
            customerTableView.setItems(foundCustomers);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Match not found");
            alert.setContentText("Unable to find matching part");
            alert.showAndWait();
        }


    }


    /**
     * Method to switch to Add Customer Menu window of application
     *
     * @param event load stage and set scene of Add Customer Menu
     * @throws IOException FXMLLoader
     */
    public void onActionAddCustomer(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/AddCustomerMenu.fxml");
    }

    /**
     * Method to switch to Update Customer Menu window of application
     *
     * @param event load stage and set scene of Update Customer Menu
     * @throws IOException FXMLLoader
     */
    public void onActionUpdateCustomer(ActionEvent event) throws IOException {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateCustomerMenu.fxml"));
            loader.load();

            UpdateCustomerMenuController UpCustomerController = loader.getController();
            UpCustomerController.sendCustomer(customerTableView.getSelectionModel().getSelectedIndex(), customerTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }

        catch (NullPointerException e) {
            MainMenuController.alertBox("No Customer Selected", "Please select a customer to update", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to check if customer has appointment
     *
     * lambda used to gather list of customers that had appointment from appointment object
     *
     * @param customer* Customer object
     * @return true if customer has appointment, else return false
     * @throws SQLException mysql
     */
    public boolean checkCustomerForAppointment(Customer customer) throws SQLException {

      ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
      ObservableList<Integer> customerWithAppointment = FXCollections.observableArrayList();

      allAppointments.forEach(appointment -> customerWithAppointment.add(appointment.getApptCustomerID()));

      for(int i = 0; i < customerWithAppointment.size(); i++){

         int customerID = customerWithAppointment.get(i);

         if(customerID == customer.getCustomerId()){
             return true;
         }
    }
        return false;
    }

    /**
     * Method to remove customer
     *
     * @param actionEvent removes customer from table and database
     * @throws SQLException mysql
     */
    public void onActionDeleteCustomer(ActionEvent actionEvent) throws SQLException {


        if (customerTableView.getSelectionModel().isEmpty()) {
            alertBox("Error: No item selected", "Please select a customer to remove.", Alert.AlertType.CONFIRMATION);
        }
        else if(checkCustomerForAppointment(customerTableView.getSelectionModel().getSelectedItem())){
            alertBox("Warning: Customer has appointment", "All appointments with this customer must be canceled before the customer can be deleted", Alert.AlertType.WARNING);
        }
        else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Action");
            alert.setContentText("Are you sure you want to remove this customer?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                int selectedCustomerID = selectedCustomer.getCustomerId();
                CustomerQuery.delete(selectedCustomerID);

                Alert notification = new Alert(Alert.AlertType.INFORMATION);
                notification.setTitle("Customer deleted");
                notification.setContentText("Selected customer was successfully deleted!");
                notification.show();

                customerTableView.setItems(CustomerQuery.getAllCustomers());
            }

        }

        }


    /**
     * Exit to Main Menu from Customer Menu
     *
     * @param event Return to Main Menu action
     * @throws IOException FXMLLoader
     */
    public void onActionExit(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/MainMenu.fxml");

    }

    /**
     * Controller initializes tp display all customers and their information to respecting columns of tableview
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {


           ObservableList<Customer> allCustomers = CustomerQuery.getAllCustomers();
           customerTableView.setItems(allCustomers);

           customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
           customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
           customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
           customerZipCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
           customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNum"));
           customerDivIDCol.setCellValueFactory(new PropertyValueFactory<>("customerDivId"));
           customerDivCol.setCellValueFactory(new PropertyValueFactory<>("customerDiv"));
           customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
