package com.trevinavery.familymap.model;

/**
 * The User class is a Java representation of a single row
 * in the Users table. This is a convenience class to help
 * process the data in the database.
 */
public class User {

    // UserName: Unique user name (non-empty string)
    private String userName;

    // Password: User’s password (non-empty string)
    private String password;

    // Email: User’s email address (non-empty string)
    private String email;

    // First Name: User’s first name (non-empty string)
    private String firstName;

    // Last Name: User’s last name (non-empty string)
    private String lastName;

    // Gender: Person’s gender (string: "f" or "m")
    private String gender;

    // Person ID: Unique Person ID assigned to this user’s generated Person object - see Family
    // History Information section for details (non-empty string)
    private String personID;

    /**
     * Constructs a User object with no data.
     */
    public User() {
        // default constructor
    }

    /**
     * Constructs a User object with pre-entered data.
     *
     * @param userName
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     * @param personID
     */
    public User(String userName, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        setUserName(userName);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPersonID(personID);
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

        User user = (User) o;

        if (userName != null ? !userName.equals(user.userName) : user.userName != null)
            return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null)
            return false;
        if (gender != null ? !gender.equals(user.gender) : user.gender != null) return false;
        return personID != null ? personID.equals(user.personID) : user.personID == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (personID != null ? personID.hashCode() : 0);
        return result;
    }
}
