package model;

/**
 * Models a country
 *
 * @author Wyatt Brock
 */
public class Country {

    /**
     * ID of country
     */
    private int countryId;

    /**
     * Name of country
     */
    private String countryName;

    /**
     * Constructor of instance of country
     *
     * @param countryId ID of country
     * @param countryName Name of country
     */
    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }

    //Getters

    /**
     * @return ID of country
     */
    public int getCountryId(){
        return countryId;
    }

    /**
     * @return Name of country
     */
    public String getCountryName(){
        return countryName;
    }


}
