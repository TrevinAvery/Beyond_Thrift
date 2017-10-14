package com.trevinavery.beyondthrift.model;

import java.util.Arrays;

/**
 * The LocationData class is a Java representation of a JSON array of location objects.
 * This is a convenience class to help process the data from JSON.
 */
public class DonationData {

    private Donation[] data;

    /**
     * Constructs an LocationData object with no data.
     */
    public DonationData() {
        // default constructor
    }

    /**
     * Constructs an LocationData object with pre-entered data.
     *
     * @param data array of locations
     */
    public DonationData(Donation[] data) {
        setData(data);
    }

    public Donation[] getData() {
        return data;
    }

    public void setData(Donation[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonationData that = (DonationData) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(data, that.data);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
