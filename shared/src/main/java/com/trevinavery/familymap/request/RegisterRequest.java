package com.trevinavery.familymap.request;

/**
 * The RegisterRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class RegisterRequest implements IRequest {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Constructs a RegisterRequest object with no data.
     */
    public RegisterRequest() {
        //default constructor
    }

    /**
     * Constructs a RegisterRequest object with pre-entered data.
     *
     * @param userName data from handler
     * @param password data from handler
     * @param email data from handler
     * @param firstName data from handler
     * @param lastName data from handler
     * @param gender data from handler
     */
    public RegisterRequest(String userName, String password, String email,
                           String firstName, String lastName, String gender) {
        setUserName(userName);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterRequest request = (RegisterRequest) o;

        if (userName != null ? !userName.equals(request.userName) : request.userName != null)
            return false;
        if (password != null ? !password.equals(request.password) : request.password != null)
            return false;
        if (email != null ? !email.equals(request.email) : request.email != null) return false;
        if (firstName != null ? !firstName.equals(request.firstName) : request.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(request.lastName) : request.lastName != null)
            return false;
        return gender != null ? gender.equals(request.gender) : request.gender == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }
}
