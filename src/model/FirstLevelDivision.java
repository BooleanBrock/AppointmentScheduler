package model;

/**
 *  Models FirstLevelDivision
 *
 * @author Wyatt Brock
 *
 */
public class FirstLevelDivision {
    /**
     * ID of division
     */
    private int divisionId;

    /**
     * Name of division
     */
    private String divisionName;

    /**
     * Country ID of division
     */
    private int divCountryId;

    /**
     * Constructor of instance of FirstLevelDivision
     *
     * @param divisionId Division ID
     * @param divisionName Division Name
     * @param divCountryId Division Country ID
     */
    public FirstLevelDivision(int divisionId, String divisionName, int divCountryId){
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.divCountryId = divCountryId;
    }

    //Getters

    /**
     * @return Division ID
     */
    public int getDivisionId(){
        return divCountryId;
    }

    /**
     * @return Division name
     */
    public String getDivisionName(){
        return divisionName;
    }

    /**
     * @return Division Country ID
     */
    public int getDivCountryId(){
        return divCountryId;
    }
}
