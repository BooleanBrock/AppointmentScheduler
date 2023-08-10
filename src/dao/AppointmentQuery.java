package dao;

import helper.TimeConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Dao class to query database appointment tables
 *
 * @author Wyatt Brock
 */
public class AppointmentQuery {


    /**
     * Method to get all appointments and appointment info
     *
     * @return Appointment object
     * @throws SQLException mysql
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        String sql = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, " +
                "appointments.User_ID, appointments.Contact_ID, contacts.Contact_Name FROM APPOINTMENTS INNER JOIN CONTACTS ON appointments.Contact_ID = contacts.Contact_ID";


        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while(rs.next()){

            int apptID = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDescription = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptType = rs.getString("Type");
            Timestamp apptStart = rs.getTimestamp("Start");
            Timestamp apptEnd = rs.getTimestamp("End");
            int apptCustomerID = rs.getInt("Customer_ID");
            int apptUserID = rs.getInt("User_ID");
            int apptContactID = rs.getInt("Contact_ID");
            String apptContactName = rs.getString("Contact_Name");


            Appointment appointment = new Appointment(apptID, apptTitle, apptDescription, apptLocation, apptType, TimeConverter.convertFromUTC(apptStart.toLocalDateTime()), TimeConverter.convertFromUTC(apptEnd.toLocalDateTime()), apptCustomerID, apptUserID, apptContactID, apptContactName);

            appointmentList.add(appointment);
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> lookupApptByContactName(String contactName) throws SQLException {

        ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments();
        ObservableList<Appointment> foundAppointment = FXCollections.observableArrayList();

        if (contactName.length() == 0) {
            foundAppointment = AppointmentQuery.getAllAppointments();
        } else {

            for (int i = 0; i < allAppointments.size(); i++) {
                if (allAppointments.get(i).getApptContactName().toUpperCase().contains(contactName.toUpperCase())) {

                    foundAppointment.add(allAppointments.get(i));
                }
            }
        }
        return foundAppointment;
    }


    /**
     * Method to add new appointment to database
     *
     * @param apptID appointment ID
     * @param apptTitle appointment Title
     * @param apptDesc appointment Description
     * @param apptLocation appointment Location
     * @param apptType appointment Type
     * @param apptStart appointment Start
     * @param apptEnd appointment End
     * @param apptCustID appointment Customer ID
     * @param apptUserID appointment User ID
     * @param apptContactID appointment Contact ID
     * @return rows of database affected by insertion
     * @throws SQLException mysql
     */
    public static int insert(int apptID, String apptTitle, String apptDesc, String apptLocation, String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustID,
                             int apptUserID, int apptContactID) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, apptID);
        ps.setString(2, apptTitle);
        ps.setString(3, apptDesc);
        ps.setString(4, apptLocation);
        ps.setString(5, apptType);
        ps.setTimestamp(6, Timestamp.valueOf(apptStart));
        ps.setTimestamp(7, Timestamp.valueOf(apptEnd));
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "admin");
        ps.setInt(10, apptCustID);
        ps.setInt(11, apptUserID);
        ps.setInt(12, apptContactID);

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to update existing record of appointment in database
     *
     * @param apptID appointment ID
     * @param apptTitle appointment Title
     * @param apptDesc appointment Description
     * @param apptLocation appointment Location
     * @param apptType appointment Type
     * @param apptStart appointment Start
     * @param apptEnd appointment End
     * @param apptCustID appointment Customer ID
     * @param apptUserID appointment User ID
     * @param apptContactID appointment Contact ID
     * @return rows affected by update
     * @throws SQLException mysql
     */
    public static int update(int apptID, String apptTitle, String apptDesc, String apptLocation, String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustID,
                             int apptUserID, int apptContactID) throws SQLException {

        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, apptTitle);
        ps.setString(2, apptDesc);
        ps.setString(3, apptLocation);
        ps.setString(4, apptType);
        ps.setTimestamp(5, Timestamp.valueOf(apptStart));
        ps.setTimestamp(6, Timestamp.valueOf(apptEnd));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, "admin");
        ps.setInt(9, apptCustID);
        ps.setInt(10, apptUserID);
        ps.setInt(11, apptContactID);
        ps.setInt(12, apptID);



        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to get all appointment User IDs
     *
     * @return list of appointment user IDs
     * @throws SQLException mysql
     */
    public static ObservableList<Integer> getAllAppointmentUserIDs() throws SQLException {
        String sql = "SELECT User_ID FROM APPOINTMENTS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Integer> appointmentUserIDList = FXCollections.observableArrayList();

        while(rs.next()){
            int apptUserID = rs.getInt("User_ID");

            appointmentUserIDList.add(apptUserID);
        }

        return appointmentUserIDList;

    }

    /**
     * Method to return all appointment Customer IDs
     *
     * @return list of all appointment Customer IDs
     * @throws SQLException mysql
     */
    public static ObservableList<Integer> getAllAppointmentCustomerIDs() throws SQLException {
        String sql = "SELECT Customer_ID FROM APPOINTMENTS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Integer> appointmentCustomerIDList = FXCollections.observableArrayList();

        while(rs.next()){

            int apptCustomerID = rs.getInt("Customer_ID");
            appointmentCustomerIDList.add(apptCustomerID);
        }

        return appointmentCustomerIDList;

    }


    /**
     * Method to delete appointment from appointment table of database
     *
     * @param apptID appointment ID
     * @return rows affected by action
     * @throws SQLException mysql
     */
    public static int delete(int apptID) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

}
