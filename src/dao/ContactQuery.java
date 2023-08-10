package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dao class to query database contact table for contact info
 *
 * @author Wyatt Brock
 */
public class ContactQuery {

    /**
     * Method to get all contacts from contact table
     *
     * @return all contact objects
     * @throws SQLException mysql
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {

        String sql = "SELECT Contact_ID, Contact_Name, Email FROM CONTACTS ";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        while(rs.next()){

            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");

            Contact contact = new Contact(contactID, contactName, contactEmail );

            allContacts.add(contact);
        }


        return allContacts;
    }

    /**
     * Method to return contact ID corresponding to contact name
     *
     * @param contactName Name of contact
     * @return integer ID of contact
     * @throws SQLException mysql
     */
    public static int getContactID(String contactName) throws SQLException {

        String sql = "SELECT Contact_ID FROM CONTACTS WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, contactName);

        ResultSet rs = ps.executeQuery();

        do {
            if (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                return contactID;
            } else {
                return 1;
            }
        }
        while (rs.next());
    }

    /**
     * Method to get name of contact by contact ID
     *
     * @param contactID ID of contact
     * @return name of contact as a string
     * @throws SQLException mysql
     */
    public static String getContactName(int contactID) throws SQLException {

        String sql = "SELECT Contact_Name FROM CONTACTS WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, contactID);

        ResultSet rs = ps.executeQuery();

        do {
            if (rs.next()) {
                String contactName = rs.getString("Contact_Name");
                return contactName;
            } else {
                return "error";
            }
        }
        while (rs.next());
    }
}
