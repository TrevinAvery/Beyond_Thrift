package com.trevinavery.beyondthrift.request;

/**
 * The LoginRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class LoginRequest implements IRequest {

    private String userName;
    private String password;

    /**
     * Constructs a LoginRequest object with no data.
     */
    public LoginRequest() {
        // default constructor
    }

    /**
     * Constructs a LoginRequest object with pre-entered data.
     *
     * @param userName data from handler
     * @param password data from handler
     */
    public LoginRequest(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginRequest that = (LoginRequest) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        return password != null ? password.equals(that.password) : that.password == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
