package com.trevinavery.beyondthrift.model;

/**
 * The Location class is a Java representation of a JSON location object.
 * This is a convenience class to help process the data from JSON.
 */
public class Location {

    private String type;
    private String title;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String latitude;
    private String longitude;
    private String phone;
    private String hours;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * Constructs an Location object with no data.
     */
    public Location() {
        // default constructor
    }

    /**
     * Constructs a Location object with pre-entered data.
     *
     * @param state
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String type, String title, String street, String city, String state, String zip, String latitude, String longitude, String phone, String hours) {
        setType(type);
        setTitle(title);
        setStreet(street);
        setCity(city);
        setState(state);
        setZip(zip);
        setLatitude(latitude);
        setLongitude(longitude);
        setPhone(phone);
        setHours(hours);
    }

    /**
     * Constructs a Location object with pre-entered data.
     *
     * @param state
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String type, String title, String street, String city, String state, String zip, double latitude, double longitude, String phone, String hours) {
        setType(type);
        setTitle(title);
        setStreet(street);
        setCity(city);
        setState(state);
        setZip(zip);
        setLatitude(latitude);
        setLongitude(longitude);
        setPhone(phone);
        setHours(hours);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

        if (state != null ? !state.equals(location.state) : location.state != null)
            return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (latitude != null ? !latitude.equals(location.latitude) : location.latitude != null)
            return false;
        return longitude != null ? longitude.equals(location.longitude) : location.longitude == null;

    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getType() {
        return type;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getAddress() {
        return street + "\n" + city + ", " + state + " " + zip;
    }
}
