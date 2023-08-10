package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class to query database users table
 *
 * @author Wyatt Brock
 */
public abstract class UserQuery {

    /**
     * Method to insert new user into to database
     *
     * @param userId ID of user
     * @param userName Username
     * @return rows affected by insertion of new user
     * @throws SQLException mysql
     */
    public static int insert(int userId, String userName) throws SQLException {
        String sql = "INSERT INTO USERS (User_ID, User_Name) VALUES(?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, userName);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to get all user IDs as list of integers
     *
     * @return list of user IDs as integers
     * @throws SQLException mysql
     */
    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        String sql = "SELECT User_ID FROM USERS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Integer> userIDList = FXCollections.observableArrayList();

        while(rs.next()){
            int userID = rs.getInt("User_ID");

            userIDList.add(userID);
        }

        return userIDList;

    }

    /**
     * Method to update existing user record
     *
     * @param userId ID of user
     * @param userName Name of user
     * @return rows affected by update
     * @throws SQLException mysql
     */
    public static int update(int userId, String userName) throws SQLException {

        String sql = "UPDATE USERS SET User_Name = ? WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(2, userId);
        ps.setString(1, userName);

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to delete user record from database table users
     *
     * @param userId ID of user
     * @return rows affected by deletion
     * @throws SQLException mysql
     */
    public static int delete(int userId) throws SQLException {
        String sql = "DELETE FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Method to get all data on user from database table users
     *
     * @throws SQLException mysql
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("User_Id");
            String userName = rs.getString("User_Name");
            System.out.print(userId + " | ");
            System.out.print(userName + "\n");

        }
    }

    /**
     * Method to check for User ID by username
     *
     * @param userName username
     * @return ID of user as integer
     * @throws SQLException mysql
     */
    public static int checkUserIDByName(String userName) throws SQLException {

        String sql = "SELECT User_ID FROM USERS WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);

        ResultSet rs = ps.executeQuery();

        do {
            if (rs.next()) {

                return rs.getInt("User_ID");
            } else {
                return 0;
            }
        } while (rs.next()) ;
    }

    /**
     * Method to get username by User ID
     *
     * @param userID ID of User
     * @return User username as string
     * @throws SQLException mysql
     */
    public static String getUserNameByID(int userID) throws SQLException {

        String sql = "SELECT User_Name FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);

        ResultSet rs = ps.executeQuery();

        do {
            if (rs.next()) {

                return rs.getString("User_Name");
            } else {
                return null;
            }
        } while (rs.next()) ;
    }


    /**
     * Method to get all info on users from users table in database using username and pasword
     *
     * @param userName username
     * @param password password
     * @throws SQLException mysql
     */
    public static void select(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_Id");
            String user = rs.getString("User_Name");
            String passWord = rs.getString("Password");
            System.out.print(userId + " | ");
            System.out.print(user + " | ");
            System.out.print(passWord + "\n");
            }
    }

    /**
     * Method to verify user login information using database table users
     *
     * @param userName username
     * @param password password
     * @return boolean, true if user found, else returns false
     * @throws SQLException mysql
     */
    public static boolean loginCheck(String userName, String password) throws SQLException{

        String sql = "SELECT User_ID, User_Name FROM USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();


            do {
                if (rs.next()) {
                    int userId = rs.getInt("User_Id");
                    String user = rs.getString("User_Name");


                    System.out.print(userId + " | ");
                    System.out.print(user + "\n");
                    return true;
                } else {
                    return false;
                }

            } while (rs.next()) ;
    }
}
