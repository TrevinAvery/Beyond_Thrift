package com.trevinavery.beyondthrift.model;

import java.util.Arrays;

/**
 * The NameData class is a Java representation of a JSON array of Strings. This is a
 * convenience class to help process the data from JSON.
 */
public class NameData {

    private String[] data;

    /**
     * Constructs an NameData object with no data.
     */
    public NameData() {
        // default constructor
    }

    /**
     * Constructs an NameData object with pre-entered data.
     *
     * @param data
     */
    public NameData(String[] data) {
        setData(data);
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameData nameData = (NameData) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(data, nameData.data);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
