package model;

/**
 * Provides constructor, getters, and setters for contact object
 *
 * @author Wyatt Brock
 */
public class Contact {

    /**
     * ID of contact
     */
    private int contactId;

    /**
     * Name of contact
     */
    private String contactName;

    /**
     * Email of contact
     */
    private String contactEmail;

    /**
     * Constructor for instance of contact
     *
     * @param contactId ID of contact
     * @param contactName Name of contact
     * @param contactEmail Email of contact
     */
    public Contact(int contactId, String contactName, String contactEmail){
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    //Getters

    /**
     * @return ID of contact
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return Name of contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return Email of contact
     */
    public String getContactEmail() {
        return contactEmail;
    }
}
