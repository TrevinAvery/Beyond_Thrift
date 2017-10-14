package com.trevinavery.beyondthrift.model;

/**
 * The Person class is a Java representation of a single row
 * in the Persons table. This is a convenience class to help
 * process the data in the database.
 */
public class Donation {

    // Person ID: Unique identifier for this person (non-empty string)
    private String description;

    // Descendant: User (Username) to which this person belongs
    private String size;

    // First Name: Person’s first name (non-empty string)
    private String location;

    // Last Name: Person’s last name (non-empty string)
    private String start;

    // Gender: Person’s end (string: "f" or "m")
    private String end;


    /**
     * Constructs a Person object with no data.
     */
    public Donation() {
        // default constructor
    }

    /**
     * Constructs a Person object with pre-entered data.
     *
     * @param personID
     * @param descendant
     * @param firstName
     * @param lastName
     * @param gender
     */
    public Donation(String personID, String descendant, String firstName, String lastName,
                    String gender) {
        setDescription(personID);
        setSize(descendant);
        setLocation(firstName);
        setStart(lastName);
        setEnd(gender);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
