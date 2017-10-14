package com.trevinavery.familymap.model;

import java.util.Arrays;

/**
 * The LocationData class is a Java representation of a JSON array of location objects.
 * This is a convenience class to help process the data from JSON.
 */
public class LocationData {

    private Location[] data;

    /**
     * Constructs an LocationData object with no data.
     */
    public LocationData() {
        // default constructor
    }

    /**
     * Constructs an LocationData object with pre-entered data.
     *
     * @param data array of locations
     */
    public LocationData(Location[] data) {
        setData(data);
    }

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationData that = (LocationData) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(data, that.data);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
