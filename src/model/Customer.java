package model;

/**
 * Models a customer
 */
public class Customer {

    /**
     * ID of customer
     */
    private int customerId;

    /**
     * Name of customer
     */
    private String customerName;

    /**
     * Postal code of customer
     */
    private String customerPostalCode;

    /**
     * Address of customer
     */
    private String customerAddress;

    /**
     * Phone number of customer
     */
    private String customerPhoneNum;

    /**
     * Division ID of customer
     */
    private int customerDivId;

    /**
     * Division name of customer
     */
    private String customerDiv;

    /**
     * Country ID of customer
     */
    private int customerCountryID;

    /**
     * Name of country of customer
     */
    private String customerCountry;

    /**
     * Constructor for instance of customer
     *
     * @param customerId ID of customer
     * @param customerName Name of customer
     * @param customerPostalCode Postal code of customer
     * @param customerAddress Address of customer
     * @param customerPhoneNum Phone number of customer
     * @param customerDivId Division ID of customer
     * @param customerDiv Division name of customer
     * @param customerCountryID Country ID of customer
     * @param customerCountry Name of country of customer
     */
    public Customer(int customerId, String customerName, String customerPostalCode, String customerAddress, String customerPhoneNum, int customerDivId, String customerDiv, int customerCountryID, String customerCountry){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPostalCode = customerPostalCode;
        this.customerAddress = customerAddress;
        this.customerPhoneNum = customerPhoneNum;
        this.customerDivId = customerDivId;
        this.customerDiv = customerDiv;
        this.customerCountryID = customerCountryID;
        this.customerCountry = customerCountry;
    }

    //Getters and setters

    /**
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId Customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return Name of customer
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return Customer postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * @param customerPostalCode Customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * @return Customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress Customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return Customer phone number
     */
    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    /**
     * @param customerPhoneNum Customer phone number
     */
    public void setCustomerPhoneNum(String customerPhoneNum) {
        this.customerPhoneNum = customerPhoneNum;
    }

    /**
     * @return Customer division ID
     */
    public int getCustomerDivId() {
        return customerDivId;
    }

    /**
     * @param customerDivId Customer division ID
     */
    public void setCustomerDivId(int customerDivId) {
        this.customerDivId = customerDivId;
    }

    /**
     * @return Customer division
     */
    public String getCustomerDiv(){
        return customerDiv;
    }

    /**
     * Customer division
     */
    public void setCustomerDiv(){
        this.customerDiv = customerDiv;
    }

    /**
     * @return Customer country ID
     */
    public int getCustomerCountryID(){ return customerCountryID; }

    /**
     * @return Customer country
     */
    public String getCustomerCountry(){ return customerCountry; }
}
