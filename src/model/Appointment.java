package model;




import java.time.LocalDateTime;


/**
 * Class to create appointment object
 */
public class Appointment {

    /**
     * Appointment contact name
     */
    private String apptContactName;
    /**
     * Appointment ID
     */
    private int apptId;

    /**
     * Appointment title
     */
    private String apptTitle;

    /**
     * Appointment description
     */
    private String apptDescription;

    /**
     * Appointment location
     */
    private String apptLocation;

    /**
     * Appointment type
     */
    private String apptType;

    /**
     * Appointment start date and time
     */
    private LocalDateTime apptStart;

    /**
     * Appointment end date and time
     */
    private LocalDateTime apptEnd;

    /**
     * Appointment customer ID
     */
    private int apptCustomerID;

    /**
     * Appointment user ID
     */
    private int apptUserId;

    /**
     * Appointment contact ID
     */
    private int apptContactId;


    /**
     * Constructor for new instance of Appointment
     *
     * @param apptId Appointment ID
     * @param apptTitle Appointment Title
     * @param apptDescription Appointment description
     * @param apptLocation Appointment location
     * @param apptType Appointment type
     * @param apptStart Apointment start date and time
     * @param apptEnd Appointment end date and time
     * @param apptCustomerID Appointment customer ID
     * @param apptUserId Appointment user ID
     * @param apptContactId Appointment contact ID
     * @param apptContactName Appointment contact name
     */
    public Appointment(int apptId, String apptTitle, String apptDescription, String apptLocation, String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustomerID, int apptUserId, int apptContactId, String apptContactName)  {
        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerID = apptCustomerID;
        this.apptUserId = apptUserId;
        this.apptContactId = apptContactId;
        this.apptContactName = apptContactName;



    }

    //Getters

    /**
     * @return Appointment ID
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * @return Appointment title
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**
     * @return appointment description
     */
    public String getApptDescription() {
        return apptDescription;
    }

    /**
     * @return apointment location
     */
    public String getApptLocation() {
        return apptLocation;
    }

    /**
     * @return appointment type
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * @return appointment start date and time
     */
    public LocalDateTime getApptStart() {
        return apptStart;
    }

    /**
     * @return appointment end date and time
     */
    public LocalDateTime getApptEnd() {
        return apptEnd;
    }

    /**
     * @return appointment customer ID
     */
    public int getApptCustomerID() {
        return apptCustomerID;
    }

    /**
     * @return appointment user ID
     */
    public int getApptUserId() {
        return apptUserId;
    }

    /**
     * @return appointment contact ID
     */
    public int getApptContactId() {
        return apptContactId;
    }

    /**
     * @return appointment contact name
     */
    public String getApptContactName() { return apptContactName; }




}
