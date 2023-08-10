package controller;

import dao.CountryQuery;
import dao.CustomerQuery;
import dao.FirstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *Controller class that gives control logic for add customer window within application
 *
 * @author Wyatt Brock
 */
public class AddCustomerMenuController implements Initializable {

    /**
     * Customer country combo box
     */
    @FXML
    private ComboBox<String> customerCountryComboBox;
    /**
     * Customer division combo box
     */
    @FXML
    private ComboBox<String> customerStateComboBox;
    /**
     * Customer ID text field
     */
    @FXML
    private TextField customerIdTxt;
    /**
     * Customer name text field
     */
    @FXML
    private TextField customerNameTxt;
    /**
     * Customer postal code text field
     */
    @FXML
    private TextField customerPostalCodeTxt;
    /**
     * Customer address text field
     */
    @FXML
    private TextField customerAddressTxt;
    /**
     * Customer phone number text field
     */
    @FXML
    private TextField customerPhoneTxt;

    Stage stage;

    Parent scene;


    /**
     * @param event On action loads another window with provided source
     * @param source String to provide source for FXMLoader
     * @throws IOException FXMLLoader
     */
    public void sceneLoader(ActionEvent event, String source) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        scene = FXMLLoader.load(getClass().getResource(source));

        stage.setScene(new Scene(scene));

        stage.show();
    }

    /**
     * Method to generate customer ID
     *
     * @return int Customer ID
     * @throws SQLException mysql
     */
    public int getCustomerID() throws SQLException {
        ObservableList<Customer> allCustomers = CustomerQuery.getAllCustomers();
        ObservableList<Integer> allCustomerId = FXCollections.observableArrayList();

        allCustomers.forEach(customer -> allCustomerId.add(customer.getCustomerId()));


        int addCustomerID;

        if (allCustomerId.size() != 0) {
            addCustomerID = allCustomerId.get(allCustomerId.size() - 1) + 1 ;
        } else {
            addCustomerID = 1;
        }
        return addCustomerID;
    }

    /**
     * Method to save new customer
     *
     * @param event on action saves customer information and goes to Customer Menu window
     * @throws SQLException mysql
     * @throws IOException FXMLLoader
     */
    public void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {

        if (customerNameTxt.getText().isEmpty() || customerAddressTxt.getText().isEmpty() || customerPostalCodeTxt.getText().isEmpty() || customerPhoneTxt.getText().isEmpty()
                || customerCountryComboBox.getSelectionModel().getSelectedItem() == null || customerStateComboBox.getSelectionModel().getSelectedItem() == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No input");
            alert.setContentText("Please input value into all fields");
            alert.showAndWait();
        }

        else {

            int addCustomerID = getCustomerID();
            String addCustomerName = customerNameTxt.getText();
            String addCustomerAddress = customerAddressTxt.getText();
            String addCustomerPostalCode = customerPostalCodeTxt.getText();
            String addCustomerPhoneNum = customerPhoneTxt.getText();

            String customerSelectedDivision = customerStateComboBox.getSelectionModel().getSelectedItem();

            int addCustomerDivID = FirstLevelDivisionQuery.getDivisionID(customerSelectedDivision);

            String customerCountry = customerCountryComboBox.getSelectionModel().getSelectedItem().toString();

            CustomerQuery.insert(addCustomerID, addCustomerName, addCustomerAddress, addCustomerPostalCode, addCustomerPhoneNum, addCustomerDivID);

            sceneLoader(event, "/view/CustomerMenu.fxml");

        }
    }


    /**
     * Method to return to Customer Menu
     *
     * @param event on action returns to Customer Menu
     * @throws IOException FXMLLoader
     */
    public void onActionReturnToCustomerMenu(ActionEvent event) throws IOException {

        sceneLoader(event, "/view/CustomerMenu.fxml");


    }

    /**
     * Method to filter divisions based on selected country
     *
     * @param event on action filters divisions based on selected country
     */
    public void onActionFilterDivisions(ActionEvent event) {

        //Filters divisions to states of the U.S.
        Boolean unitedStatesSelected = customerCountryComboBox.getSelectionModel().getSelectedItem().equals("U.S");
        //Filters divisions to locations within U.K.
        Boolean unitedKingdomSelected = customerCountryComboBox.getSelectionModel().getSelectedItem().equals("UK");


        if (unitedKingdomSelected) {
            try {
                customerStateComboBox.setItems(null);
                customerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedKingdomDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (unitedStatesSelected) {
            try {
                customerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedStatesDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                //Filters divisions to provinces of Canada
                customerStateComboBox.setItems(FirstLevelDivisionQuery.getCanadaDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }


    /**
     * Controller initializes to add all countries to corresponding combo box
     *
     * lambda expression to gather all country names into list from country objects
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            ObservableList<Country> allCountries = CountryQuery.getAllCountries();
            ObservableList<String> allCountryNames = FXCollections.observableArrayList();

            //lambda #2
            allCountries.forEach(country -> allCountryNames.add(country.getCountryName()));

            customerCountryComboBox.setItems(allCountryNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}








