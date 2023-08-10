package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dao class to query database table of countries
 *
 * @author Wyatt Brock
 */
public class CountryQuery {

    /**
     * Method to get all country objects from database country table
     *
     * @return all country objects
     * @throws SQLException mysql
     */
    public static ObservableList<Country> getAllCountries() throws SQLException{

        String sql = "SELECT Country_ID, Country FROM COUNTRIES";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Country> allCountries = FXCollections.observableArrayList();

        while(rs.next()){

            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Country country = new Country(countryId, countryName);

            allCountries.add(country);
        }


        return allCountries;
    }

    /**
     * Method to get country object by country ID
     *
     * @param inputCountryID  ID of country
     * @return country object
     * @throws SQLException mysql
     */
    public static ObservableList<Country> getCountryByID(int inputCountryID) throws SQLException {

        String sql = "SELECT Country, Country_ID FROM COUNTRIES WHERE Country_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, inputCountryID);
        ResultSet rs = ps.executeQuery();

        ObservableList<Country> countryByID = FXCollections.observableArrayList();

        while(rs.next()){
            String countryName = rs.getString("Country");
            int countryID = rs.getInt("Country_ID");

            Country country = new Country(countryID, countryName);
            countryByID.add(country);
        }
        return countryByID;
    }

    /**
     * Method to get names of all countries
     *
     * @return name of all country objects as a string
     */
   public static ObservableList<String> allCountryNames(){
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
       return countryNameList;
   }

}
