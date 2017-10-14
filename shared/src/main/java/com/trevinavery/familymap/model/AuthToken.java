package com.trevinavery.familymap.model;

/**
 * The AuthToken class is a Java representation of a single row
 * in the AuthTokens table. This is a convenience class to help
 * process the data in the database.
 */
public class AuthToken {

    private String authToken;
    private String userName;
    private long dateCreated;

    /**
     * Constructs a AuthToken object with no data.
     */
    public AuthToken() {
        // default constructor
    }

    /**
     * Constructs a AuthToken object with pre-entered data.
     *
     * @param authToken
     * @param userName
     * @param dateCreated
     */
    public AuthToken(String authToken, String userName, long dateCreated) {
        setAuthToken(authToken);
        setUserName(userName);
        setDateCreated(dateCreated);
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

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken1 = (AuthToken) o;

        if (dateCreated != authToken1.dateCreated) return false;
        if (authToken != null ? !authToken.equals(authToken1.authToken) : authToken1.authToken != null)
            return false;
        return userName != null ? userName.equals(authToken1.userName) : authToken1.userName == null;

    }

    @Override
    public int hashCode() {
        int result = authToken != null ? authToken.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (int) (dateCreated ^ (dateCreated >>> 32));
        return result;
    }
}
