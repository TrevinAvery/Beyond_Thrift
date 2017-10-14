package com.trevinavery.familymap.request;

/**
 * The PersonRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class PersonRequest implements IRequest {

    private String authToken;
    private String personID;

    /**
     * Constructs a PersonRequest object with no data.
     */
    public PersonRequest() {
        // default constructor
    }

    /**
     * Constructs a PersonRequest object with pre-entered data.
     *
     * @param authToken data from handler
     */
    public PersonRequest(String authToken) {
        this(authToken, null);
    }

    /**
     * Constructs a PersonRequest object with pre-entered data.
     *
     * @param authToken data from handler
     * @param personID data from handler
     */
    public PersonRequest(String authToken, String personID) {
        setAuthToken(authToken);
        setPersonID(personID);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonRequest request = (PersonRequest) o;

        if (authToken != null ? !authToken.equals(request.authToken) : request.authToken != null)
            return false;
        return personID != null ? personID.equals(request.personID) : request.personID == null;

    }

    @Override
    public int hashCode() {
        int result = authToken != null ? authToken.hashCode() : 0;
        result = 31 * result + (personID != null ? personID.hashCode() : 0);
        return result;
    }
}
