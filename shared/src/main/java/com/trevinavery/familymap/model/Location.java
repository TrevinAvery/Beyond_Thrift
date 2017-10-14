package com.trevinavery.familymap.model;

/**
 * The Location class is a Java representation of a JSON location object.
 * This is a convenience class to help process the data from JSON.
 */
public class Location {

    private String country;
    private String city;
    private String latitude;
    private String longitude;

    /**
     * Constructs an Location object with no data.
     */
    public Location() {
        // default constructor
    }

    /**
     * Constructs an Location object with pre-entered data.
     *
     * @param country
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String country, String city, String latitude, String longitude) {
        setCountry(country);
        setCity(city);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * Constructs an Location object with pre-entered data.
     *
     * @param country
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String country, String city, double latitude, double longitude) {
        setCountry(country);
        setCity(city);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = Double.toString(latitude);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = Double.toString(longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (country != null ? !country.equals(location.country) : location.country != null)
            return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (latitude != null ? !latitude.equals(location.latitude) : location.latitude != null)
            return false;
        return longitude != null ? longitude.equals(location.longitude) : location.longitude == null;

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
