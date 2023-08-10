package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dao class to query database table of firstleveldivisions
 *
 * @author Wyatt Brock
 */
public class FirstLevelDivisionQuery {

    /**
     * Method to return all FirstLevelDivision objects
     *
     * @return list of all FirstLevelDivision objects
     * @throws SQLException mysql
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException{

        String sql = "SELECT Division_ID, Division, Country_ID FROM FIRST_LEVEL_DIVISIONS";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();

        while(rs.next()){

            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDivision division = new FirstLevelDivision(divisionId, divisionName, countryId);

            allDivisions.add(division);
        }

        return allDivisions;
    }

    /**
     * Method to return country ID by division ID
     *
     * @param divID ID of division
     * @return country ID by corresponding division ID
     * @throws SQLException mysql
     */
    public static Integer getCountryIDByDivID(int divID) throws SQLException {

        String sql = "SELECT Country_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, divID);
        ResultSet rs = ps.executeQuery();


        do {
            if (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                return countryID;
            } else {
                return 1;
            }
        }
        while (rs.next());
    }


    /**
     * Method to return division ID by division name
     *
     * @param divisionName Name of division
     * @return ID of division as an integer
     * @throws SQLException mysql
     */
    public static int getDivisionID(String divisionName) throws SQLException {

        String sql = "SELECT Division_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, divisionName);

        ResultSet rs = ps.executeQuery();

        do {
            if (rs.next()) {
                int customerDivId = rs.getInt("Division_ID");
                return customerDivId;
            } else {
                return 1;
            }
        }
        while (rs.next());
    }

    /**
     * Method to get division by division ID
     *
     * @param divisionID ID of division
     * @return Division name as string
     * @throws SQLException mysql
     */
    public static ObservableList<String> getSelectedDivision(int divisionID) throws SQLException{

        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);

        ResultSet rs = ps.executeQuery();

        ObservableList<String> selectedCustomerDivisions = FXCollections.observableArrayList();

        while(rs.next()){
            String selectedDivisionName = rs.getString("Division");

            selectedCustomerDivisions.add(selectedDivisionName);
        }
        return selectedCustomerDivisions;

    }


    /**
     * Method to return all divisions with Country_ID of 1
     *
     * @return list of division names as strings
     * @throws SQLException mysql
     */
    public static ObservableList<String> getUnitedStatesDivisions() throws SQLException{

        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 1";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<String> allUnitedStatesDivisions = FXCollections.observableArrayList();

        while(rs.next()){
            String unitedStatesDivisionName = rs.getString("Division");

            allUnitedStatesDivisions.add(unitedStatesDivisionName);
        }
        return allUnitedStatesDivisions;

    }

    /**
     * Method to return all divisions with Country_ID of 2
     *
     * @return list of division names as strings
     * @throws SQLException mysql
     */
    public static ObservableList<String> getUnitedKingdomDivisions() throws SQLException{

        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 2";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<String> allUnitedKingdomDivisions = FXCollections.observableArrayList();

        while(rs.next()){
            String unitedKingdomDivisionName = rs.getString("Division");

            allUnitedKingdomDivisions.add(unitedKingdomDivisionName);
        }
        return allUnitedKingdomDivisions;

    }



    /**
     * Method to return all divisions with Country_ID of 3
     *
     * @return list of division names as strings
     * @throws SQLException mysql
     */
    public static ObservableList<String> getCanadaDivisions() throws SQLException{

        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 3";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<String> allCanadaDivisions = FXCollections.observableArrayList();

        while(rs.next()){
             String canadaDivisionName = rs.getString("Division");

             allCanadaDivisions.add(canadaDivisionName);
        }
        return allCanadaDivisions;

    }



}

