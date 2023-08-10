package controller;

import dao.AppointmentQuery;
import dao.ContactQuery;
import dao.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;


public class ReportMenuController implements Initializable {


    /**
     * Month column of tableview
     */
    @FXML
    private TableColumn monthCol;

    /**
     * Type column of tableview
     */
    @FXML
    private TableColumn typeCol;

    /**
     * Tableview displaying division and amount of customers per division
     */
    @FXML
    private TableView countryCustomerTableView;

    /**
     * Division column of table view
     */
    @FXML
    private TableColumn countryDivTableCol;

    /**
     * Customer total column of table view
     */
    @FXML
    private TableColumn customerTotalTableCol;

    /**
     * Appointment column of table view
     */
    @FXML
    private TableColumn apptTableCol;

    /**
     * Tableview that displays number of appoinments by month and type
     */
    @FXML
    private TableView monthTypeTableView;

    /**
     * Total appointments column of tableview
     */
    @FXML
    private TableColumn totalApptCol;

    /**
     * Tableview that shows all appointments filtered by contact
     */
    @FXML
    private TableView reportByContactTableView;

    /**
     * Appointment ID column of table view
     */
    @FXML
    private TableColumn reportApptIDCol;

    /**
     * Appointment title column of table view
     */
    @FXML
    private TableColumn reportTitleCol;

    /**
     * Appointment Type column of table view
     */
    @FXML
    private TableColumn reportTypeCol;

    /**
     * Appointment description column of table view
     */
    @FXML
    private TableColumn reportDescCol;

    /**
     * Appointment start column of tableview
     */
    @FXML
    private TableColumn reportStartCol;

    /**
     * Appointment end column of tableview
     */
    @FXML
    private TableColumn reportEndCol;

    /**
     * Appointment customer ID column of tableview
     */
    @FXML
    private TableColumn reportCustIDCol;

    /**
     * Return to main menu button of Report Menu
     */
    @FXML
    private Button reportMainMenuBtn;

    /**
     * Combo box for selecting contacts by which the tableview is filtered
     */
    @FXML
    private ComboBox reportContactComboBox;


    /**
     * Method to filter apopintments by contact
     *
     * @param event selection in combo box
     * @throws SQLException mysql
     */
    public void onActionFilterByContact(ActionEvent event) throws SQLException {

        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        String contactName = reportContactComboBox.getSelectionModel().getSelectedItem().toString();

        contactAppointments = AppointmentQuery.lookupApptByContactName(contactName);

        reportByContactTableView.setItems(null);
        reportByContactTableView.setItems(contactAppointments);
    }


    /**
     * Method to return to Main Menu window
     *
     * @param event return to Main Menu button click
     * @throws IOException FXMLLoader
     */
    public void onActionReturntoMainMenu(ActionEvent event) throws IOException {

        MainMenuController.sceneLoader(event, "/view/MainMenu.fxml");

    }


    /**
     * Controller initializes to display report info to respecting three tables,
     * code for each table divided by dashes that are commented out.
     *
     * @param url Location to resolve relative path to root, or null if unknown
     * @param resourceBundle used to find location of root object, or null if none localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
            ObservableList<Report> allReports = FXCollections.observableArrayList();
            Comparator<Report> reportMonthComparator = Comparator.comparing(Report::getReportMonth);
            Comparator<Report> reportTypeComparator = Comparator.comparing(Report:: getReportType);


            for (Appointment appointment : allAppointments) {
                String month = appointment.getApptStart().getMonth().toString();
                String type = appointment.getApptType();
                int totalAppointments = Collections.frequency(allAppointments, appointment);
                Report report = new Report(month, type, null, null, totalAppointments);
                allReports.add(report);
            }

            allReports.sort(reportMonthComparator);
            allReports.sort(reportTypeComparator);

            for(int i = 0; i < allReports.size() - 1; i++) {

                String reportType = allReports.get(i).getReportType();
                String compareType = allReports.get(i + 1).getReportType();
                String reportMonth = allReports.get(i).getReportMonth().toString();
                String compareMonth = allReports.get(i + 1).getReportMonth().toString();

                if (reportType.equalsIgnoreCase(compareType)) {
                    if (reportMonth.equalsIgnoreCase(compareMonth)) {
                        allReports.remove(i + 1);
                        allReports.get(i).incrementReportNum();
                    }
                }
            }


            monthCol.setCellValueFactory(new PropertyValueFactory<>("reportMonth"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
            totalApptCol.setCellValueFactory(new PropertyValueFactory<>("reportNum"));

            monthTypeTableView.setItems(allReports);



        //-----------------------------------------------------------------------------------



            ObservableList<Contact> allContacts = ContactQuery.getAllContacts();
            ObservableList<String> allContactNames = FXCollections.observableArrayList();

            //lambda #1
            allContacts.forEach(contact -> allContactNames.add(contact.getContactName()));

            reportContactComboBox.setItems(allContactNames);


            reportByContactTableView.setItems(allAppointments);


            reportApptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            reportTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            reportDescCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));

            reportStartCol.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
            reportEndCol.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
            reportCustIDCol.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));


            //--------------------------------------------------------------------------------------------

            ObservableList<Customer> allCustomers = CustomerQuery.getAllCustomers();
            ObservableList<String> allDivisions = FXCollections.observableArrayList();
            ObservableList<String> uniqueDivisions = FXCollections.observableArrayList();
            ObservableList<Report> allReportDivisions = FXCollections.observableArrayList();


            countryDivTableCol.setText("Division");

            countryDivTableCol.setCellValueFactory(new PropertyValueFactory<>("reportDivision"));
            customerTotalTableCol.setCellValueFactory(new PropertyValueFactory<>("reportNum"));

            allCustomers.forEach(customer -> allDivisions.add(customer.getCustomerDiv()));

            for(Customer customer: allCustomers){
                String distinctDivision = customer.getCustomerDiv();
                if(!uniqueDivisions.contains(distinctDivision)){
                    uniqueDivisions.add(distinctDivision);
                }
            }

            for(String division: uniqueDivisions){
                String divToBeSet = division;
                int totalCustomers = Collections.frequency(allDivisions, divToBeSet);
                Report reportByDivision = new Report(null, null, null ,divToBeSet, totalCustomers);
                allReportDivisions.add(reportByDivision);
            }


            countryCustomerTableView.setItems(allReportDivisions);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

