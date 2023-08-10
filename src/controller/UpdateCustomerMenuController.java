package controller;

import dao.CountryQuery;
import dao.CustomerQuery;
import dao.FirstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller class that gives control logic for Update Customer window within application
 *
 * @author Wyatt Brock
 */
public class UpdateCustomerMenuController {
    /**
     * ID of customer
     */
    @FXML
    private TextField updateCustomerIdTxt;
    /**
     * Name of customer
     */
    @FXML
    private TextField updateCustomerNameTxt;
    /**
     * Postal code of customer
     */
    @FXML
    private TextField updateCustomerPostalCodeTxt;
    /**
     * Address of customer
     */
    @FXML
    private TextField updateCustomerAddressTxt;
    /**
     * Phone number of customer
     */
    @FXML
    private TextField updateCustomerPhoneTxt;
    /**
     * Country of customer
     */
    @FXML
    private ComboBox updateCustomerCountryComboBox;
    /**
     * Division of customer
     */
    @FXML
    private ComboBox updateCustomerStateComboBox;

    private int customerCurrentIndex = 0;

    /**
     * Method to filter division based on selected country of customer
     *
     * @param actionEvent country combo box selection
     */
    public void onActionFilterDivisions(ActionEvent actionEvent) {
        Boolean unitedStatesSelected = updateCustomerCountryComboBox.getSelectionModel().getSelectedItem().equals("U.S");
        Boolean unitedKingdomSelected = updateCustomerCountryComboBox.getSelectionModel().getSelectedItem().equals("UK");


        if(unitedKingdomSelected){
            try {
                updateCustomerStateComboBox.setItems(null);
                updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedKingdomDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(unitedStatesSelected){
            try {
                updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedStatesDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        else{
            try {
                updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getCanadaDivisions());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Method to load customer to Update Customer window of application from Customer Menu
     *
     * @param selectedIndex selected customer from table
     * @param selectedCustomer selected customer from table
     * @throws SQLException mysql
     */
    public void sendCustomer(int selectedIndex, Customer selectedCustomer ) throws SQLException {

        int customerCurrentIndex = selectedIndex;


        ObservableList<Country> countryList = null;
        try {
            countryList = CountryQuery.getAllCountries();

        } catch (SQLException e) {

        }
        ObservableList<String> countryNameList = FXCollections.observableArrayList();

        for(int i = 0; i < countryList.size(); i++){

            String countryName = countryList.get(i).getCountryName();

            countryNameList.add(countryName);
        }

        ObservableList<FirstLevelDivision> divisionList = null;
        try {
            divisionList = FirstLevelDivisionQuery.getAllDivisions();

        } catch (SQLException e) {

        }
        ObservableList<String> divisionNameList = FXCollections.observableArrayList();

        for(int i = 0; i < divisionList.size(); i++){

            String divisionName = divisionList.get(i).getDivisionName();

            divisionNameList.add(divisionName);
        }


        updateCustomerCountryComboBox.setItems(countryNameList);
        updateCustomerStateComboBox.setItems(divisionNameList);

        if(selectedCustomer.getCustomerCountry().equalsIgnoreCase("U.S")){
            int chosenDivision = (selectedCustomer.getCustomerDivId()) - 1;
            System.out.println(selectedCustomer.getCustomerDivId());
            updateCustomerCountryComboBox.getSelectionModel().select(0);
            updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedStatesDivisions());
            updateCustomerStateComboBox.getSelectionModel().select(chosenDivision);
        }
        if(selectedCustomer.getCustomerCountry().equalsIgnoreCase("UK")){
            int chosenDivision = (selectedCustomer.getCustomerDivId()) - 101;
            System.out.println("\n" + chosenDivision);
            updateCustomerCountryComboBox.getSelectionModel().select(1);
            updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getUnitedKingdomDivisions());
            updateCustomerStateComboBox.getSelectionModel().select(chosenDivision);

        }
        if(selectedCustomer.getCustomerCountry().equalsIgnoreCase("Canada")){
            int chosenDivision = (selectedCustomer.getCustomerDivId()) - 60;
            System.out.println("\n" + chosenDivision);
            updateCustomerCountryComboBox.getSelectionModel().select(2);
            updateCustomerStateComboBox.setItems(FirstLevelDivisionQuery.getCanadaDivisions());
            updateCustomerStateComboBox.getSelectionModel().select(chosenDivision);
        }


        updateCustomerIdTxt.setText(String.valueOf(selectedCustomer.getCustomerId()));
        updateCustomerNameTxt.setText(selectedCustomer.getCustomerName());
        updateCustomerPostalCodeTxt.setText(selectedCustomer.getCustomerPostalCode());
        updateCustomerAddressTxt.setText(selectedCustomer.getCustomerAddress());
        updateCustomerPhoneTxt.setText(selectedCustomer.getCustomerPhoneNum());

    }

    /**
     * Method to save, update, and display customer info in database and tables
     *
     * @param event save action click
     * @throws SQLException mysql
     * @throws IOException FXMLLoader
     */
    public void onActionSaveUpdatedCustomer(ActionEvent event) throws SQLException, IOException {

        if (updateCustomerNameTxt.getText().isEmpty() || updateCustomerAddressTxt.getText().isEmpty() || updateCustomerPostalCodeTxt.getText().isEmpty() || updateCustomerPhoneTxt.getText().isEmpty()
        || updateCustomerStateComboBox.getSelectionModel().getSelectedItem() == null || updateCustomerCountryComboBox.getSelectionModel().getSelectedItem() == null ) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No input");
            alert.setContentText("Please input value into all fields");
            alert.showAndWait();
        } else {

            ObservableList<Customer> allCustomers = CustomerQuery.getAllCustomers();
            ArrayList<Integer> allCustomerId = new ArrayList<>();

            for (int i = 0; i < allCustomers.size(); i++) {
                int customerId = allCustomers.get(i).getCustomerId();
                allCustomerId.add(customerId);
            }

            int updateCustomerID = Integer.parseInt(updateCustomerIdTxt.getText());
            String updateCustomerName = updateCustomerNameTxt.getText();
            String updateCustomerAddress = updateCustomerPostalCodeTxt.getText();
            String updateCustomerPostalCode = updateCustomerAddressTxt.getText();
            String updateCustomerPhoneNum = updateCustomerPhoneTxt.getText();

            String updatedCustomerSelectedDivision = updateCustomerStateComboBox.getSelectionModel().getSelectedItem().toString();

            int updatedCustomerDivID = FirstLevelDivisionQuery.getDivisionID(updatedCustomerSelectedDivision);



            CustomerQuery.update(updateCustomerID, updateCustomerName, updateCustomerAddress, updateCustomerPostalCode, updateCustomerPhoneNum, updatedCustomerDivID);

            MainMenuController.sceneLoader(event, "/view/CustomerMenu.fxml");
        }

    }

    /**
     * Method to return to Customer Menu window of application
     *
     * @param event cancel button click
     * @throws IOException FXMLLoader
     */
    public void onActionUpdateReturnToCustomerMenu(ActionEvent event) throws IOException {
        MainMenuController.sceneLoader(event, "/view/CustomerMenu.fxml");
    }
}
