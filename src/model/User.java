package model;

/**
 * Models user
 *
 * @author Wyatt Brock
 */
public class User {

    /**
     * ID of user
     */
    private int userId;

    /**
     * User username
     */
    private String userName;

    /**
     * User password
     */
    private String password;

    /**
     * Constructor for instance of user
     *
     * @param userId ID of user
     * @param userName User username
     * @param password User password
     */
    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    //Getters

    /**
     * @return User ID
     */
    public int getUserId(){
        return userId;
    }

    /**
     * @return Username
     */
    public String getUserName(){
        return userName;
    }

    /**
     * @return user password
     */
    public String getPassword(){
        return password;
    }





}
