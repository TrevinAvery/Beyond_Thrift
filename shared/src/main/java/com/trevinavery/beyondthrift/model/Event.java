package com.trevinavery.beyondthrift.model;

/**
 * The Event class is a Java representation of a single row
 * in the Events table. This is a convenience class to help
 * process the data in the database.
 */
public class Event implements Comparable<Event> {

    public static final String TYPE_BIRTH = "birth";
    public static final String TYPE_BAPTISM = "baptism";
    public static final String TYPE_CHRISTENING = "christening";
    public static final String TYPE_MARRIAGE = "marriage";
    public static final String TYPE_DEATH = "death";


    // Event ID: Unique identifier for this event (non-empty string)
    private String eventID;

    // Descendant: User (Username) to which this event belongs
    private String descendant;

    // Person: ID of person to which this event belongs
    private String personID;

    // Latitude: Latitude of event’s location
    private double latitude;

    // Longitude: Longitude of event’s location
    private double longitude;

    // Country: Country in which event occurred
    private String country;

    // City: City in which event occurred
    private String city;

    // EventType: Type of event (birth, baptism, christening, marriage, death, etc.)
    private String eventType;

    // Year: Year in which event occurred
    private int year;

    /**
     * Constructs an Event object with no data.
     */
    public Event() {
        // default constructor
    }

    /**
     * Constructs an Event object with pre-entered data.
     *
     * @param eventID
     * @param descendant
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String descendant, String personID,
                 double latitude, double longitude, String country,
                 String city, String eventType, int year) {
        setEventID(eventID);
        setDescendant(descendant);
        setPersonID(personID);
        setLatitude(latitude);
        setLongitude(longitude);
        setCountry(country);
        setCity(city);
        setEventType(eventType);
        setYear(year);
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (Double.compare(event.latitude, latitude) != 0) return false;
        if (Double.compare(event.longitude, longitude) != 0) return false;
        if (year != event.year) return false;
        if (eventID != null ? !eventID.equals(event.eventID) : event.eventID != null) return false;
        if (descendant != null ? !descendant.equals(event.descendant) : event.descendant != null)
            return false;
        if (personID != null ? !personID.equals(event.personID) : event.personID != null)
            return false;
        if (country != null ? !country.equals(event.country) : event.country != null) return false;
        if (city != null ? !city.equals(event.city) : event.city != null) return false;
        return eventType != null ? eventType.equals(event.eventType) : event.eventType == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = eventID != null ? eventID.hashCode() : 0;
        result = 31 * result + (descendant != null ? descendant.hashCode() : 0);
        result = 31 * result + (personID != null ? personID.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + year;
        return result;
    }

    public String getDescription() {

        return String.format("%s: %s, %s (%s)",
                getEventType(),
                getCity(),
                getCountry(),
                ((getYear() > 0) ? getYear() : "????")
        );
    }

    @Override
    public int compareTo(Event event) {

//        1. Birth events, if present, are always first (whether they have a year or not)
//        2. Events with years, sorted primarily by year, and secondarily by description
//        normalized to lower-case
//        3. Events without years sorted by description normalized to lower-case
//        4. Death events, if present, are always last (whether they have a year or not)

        // if birth or death event, ignore date
        if (!getEventType().equalsIgnoreCase(event.getEventType())) {
            if (getEventType().equalsIgnoreCase(TYPE_BIRTH)
                    || event.getEventType().equalsIgnoreCase(TYPE_DEATH)) {
                return -1;
            }
            if (getEventType().equalsIgnoreCase(TYPE_DEATH)
                    || event.getEventType().equalsIgnoreCase(TYPE_BIRTH)) {
                return 1;
            }
        }

        // events with dates come before those without dates
        if (getYear() == 0 && event.getYear() != 0) {
            return 1;
        }
        if (getYear() != 0 && event.getYear() == 0) {
            return -1;
        }

        // either both events have dates, or neither has a date
        int yearCompare = getYear() - event.getYear();
        if (yearCompare != 0) {
            return yearCompare;
        }

        // if not the same description, return it
        int descriptionCompare = getDescription().compareToIgnoreCase(event.getDescription());
        if (descriptionCompare != 0) {
            return descriptionCompare;
        }

        // sort by eventID to prevent false duplicates
        return getEventID().compareTo(event.getEventID());
    }
}
