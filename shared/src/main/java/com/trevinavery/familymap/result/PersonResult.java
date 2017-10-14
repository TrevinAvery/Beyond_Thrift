package com.trevinavery.familymap.result;

import java.util.Arrays;

import com.trevinavery.familymap.model.Person;

/**
 * The PersonResult class is a Java representation of a JSON response. This is a convenience class
 * to help process the data to be sent by the server.
 */
public class PersonResult implements IResult {

    private Person[] data;
    private String message;

    /**
     * Constructs a PersonResult object with no data.
     */
    public PersonResult() {
        // default constructor
    }

    /**
     * Constructs a PersonResult object with pre-entered data.
     *
     * @param person data from service
     */
    public PersonResult(Person person) {
        setData(new Person[] {person});
    }

    /**
     * Constructs a PersonResult object with pre-entered data.
     *
     * @param data data from service
     */
    public PersonResult(Person[] data) {
        setData(data);
    }

    /**
     * Constructs a PersonResult object with pre-entered data.
     *
     * @param message data from service
     */
    public PersonResult(String message) {
        setMessage(message);
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
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

        PersonResult that = (PersonResult) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(data, that.data)) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
