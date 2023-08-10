package model;

/**
 * Models report
 */
public class Report {

    /**
     * Month of appointment
     */
    private String reportMonth;

    /**
     * Type of appointment
     */
    private String reportType;

    /**
     * Country of customer
     */
    private String reportCountry;

    /**
     * Division of customer
     */
    private String reportDivision;

    /**
     * Number of total appointments or customers
     */
    private int reportNum;


    /**
     * Constructor for instance of report
     *
     * @param reportMonth Month of appointment
     * @param reportType Type of appointment
     * @param reportCountry Country of customer
     * @param reportDivision Division of customer
     * @param reportNum sum of appointments or customers
     */
    public Report(String reportMonth, String reportType, String reportCountry, String reportDivision, int reportNum){


        this.reportMonth = reportMonth;
        this.reportType = reportType;
        this.reportCountry = reportCountry;
        this.reportDivision = reportDivision;
        this.reportNum = reportNum;
    }

    public Report() {

    }

    //Getters and setters

    /**
     * @return month of appointment
     */
    public String getReportMonth(){
        return reportMonth;
    }

    /**
     * @param month month of appointment
     */
    public void setReportMonth(String month){
        reportMonth = month;
    }

    /**
     * @return type of apppointment
     */
    public String getReportType(){
        return reportType;
    }

    /**
     * @param typeOfReport type of appointment
     */
    public void setReportType( String typeOfReport){
        reportType = typeOfReport;
    }

    /**
     * @return Country of customer
     */
    public String getReportCountry(){ return reportCountry; }

    /**
     * @return Division of customer
     */
    public String getReportDivision() { return reportDivision; }

    /**
     * increases number of appointments for repeating types
     */
    //increment sum of total appointments
    public void incrementReportNum(){
        reportNum = reportNum + 1;
    }

    /**
     * @return total of appointments or customers
     */
    public int getReportNum(){
        return reportNum;
    }

    /**
     * @param numReport total of appointments or customers
     */
    public void setReportNum(int numReport){
        reportNum = numReport;
    }


}
