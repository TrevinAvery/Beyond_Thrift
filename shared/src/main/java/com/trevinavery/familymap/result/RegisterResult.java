package com.trevinavery.familymap.result;

/**
 * The RegisterResult class is a Java representation of a JSON response. This is a convenience class
 * to help process the data to be sent by the server.
 */
public class RegisterResult implements IResult {

    private String authToken;
    private String userName;
    private String personID;
    private String message;

    /**
     * Constructs a RegisterResult object with no data.
     */
    public RegisterResult() {
        // default constructor
    }

    /**
     * Constructs a RegisterResult object with pre-entered data.
     *
     * @param authToken data from service
     * @param userName data from service
     * @param personID data from service
     */
    public RegisterResult(String authToken, String userName, String personID) {
        setAuthToken(authToken);
        setUserName(userName);
        setPersonID(personID);
    }

    /**
     * Constructs a RegisterResult object with pre-entered data.
     *
     * @param message data from service
     */
    public RegisterResult(String message) {
        setMessage(message);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterResult that = (RegisterResult) o;

        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null)
            return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        if (personID != null ? !personID.equals(that.personID) : that.personID != null)
            return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = authToken != null ? authToken.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (personID != null ? personID.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
