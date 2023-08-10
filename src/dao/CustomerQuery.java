package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Dao class to query database table of customers
 *
 * @author Wyatt Brock
 */
public class CustomerQuery {

    /**
     * Method to return list of all customer objects from customer table
     *
     * @return list of all customer objects
     * @throws SQLException mysql
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException{

        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, " +
                "first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country FROM CUSTOMERS INNER JOIN FIRST_LEVEL_DIVISIONS ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN COUNTRIES on countries.Country_ID = first_level_divisions.Country_ID";
                ;

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while(rs.next()){

            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int customerDivId = rs.getInt("Division_ID");
            String customerDiv = rs.getString("Division");
            int customerCountryID = rs.getInt("Country_ID");
            String customerCountry = rs.getString("Country");


            Customer customer = new Customer(customerId, customerName, customerPostalCode, customerAddress, customerPhone, customerDivId, customerDiv, customerCountryID, customerCountry);

            customerList.add(customer);
        }

        System.out.print(customerList);

        return customerList;
    }

    /**
     * Method to return all customer IDs of customer objects
     *
     * @return list of all customer IDs as integers
     * @throws SQLException mysql
     */
    public static ObservableList<Integer> getAllCustomerIDs() throws SQLException {
        String sql = "SELECT Customer_ID FROM CUSTOMERS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();

        while(rs.next()){
            int customerID = rs.getInt("Customer_ID");

            customerIDList.add(customerID);
        }

        return customerIDList;

    }

    /**
     * Method to add new customer to customers table of database
     *
     * @param customerID  ID of customer
     * @param customerName Name of customer
     * @param customerAddress Address of customer
     * @param postalCode Postal code of customer
     * @param phoneNum Phone number of customer
     * @param divisionID Division ID of customer
     * @return rows affected by insertion of new customer
     * @throws SQLException mysql
     */
    public static int insert(int customerID, String customerName, String customerAddress, String postalCode, String phoneNum, int divisionID ) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, customerID);
        ps.setString(2, customerName);
        ps.setString(3, customerAddress);
        ps.setString(4, postalCode);
        ps.setString(5, phoneNum);
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "admin");
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "admin");
        ps.setInt(10, divisionID);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    public static int update(int customerID, String customerName, String customerAddress, String postalCode, String phoneNum, int divisionID ) throws SQLException {

        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6,"admin");
        ps.setInt(7, divisionID);
        ps.setInt(8, customerID);


        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to lookup customer by customer ID
     *
     * @param customerID ID of customer
     * @return customer object by corresponding ID
     * @throws SQLException mysql
     */
   public static Customer lookupCustomer(int customerID) throws SQLException {
       ObservableList<Customer> allCustomers = CustomerQuery.getAllCustomers();

        Customer placeholder = null;
        for(Customer customer: allCustomers) {

            if (customerID == customer.getCustomerId()) {
                placeholder = customer;
            }
        }
       return placeholder;
   }

    /**
     * Method to return customer by customer name
     *
     * @param customerName Name of customer
     * @return customer object by corresponding name
     * @throws SQLException mysql
     */
    public static ObservableList lookupCustomer(String customerName) throws SQLException {

        ObservableList<Customer> foundCustomer = FXCollections.observableArrayList();

        if (customerName.length() == 0) {
            foundCustomer = CustomerQuery.getAllCustomers();
        } else {
            for (int i = 0; i < CustomerQuery.getAllCustomers().size(); i++) {
                if (CustomerQuery.getAllCustomers().get(i).getCustomerName().toUpperCase().contains(customerName.toUpperCase())) {
                    foundCustomer.add(CustomerQuery.getAllCustomers().get(i));
                }
            }
        }
        return foundCustomer;
    }


    /**
     * Method to delete customer record from customer table
     *
     * @param customerId ID of customer
     * @return rows affected by deletion
     * @throws SQLException mysql
     */
    public static int delete(int customerId) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    }






