package com.trevinavery.beyondthrift.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import com.trevinavery.beyondthrift.model.Event;

/**
 * The EventDao class creates a Data Access Object that is used to
 * manipulate all event related data in the provided database.
 */
public class EventDao extends AbstractDao {

    /**
     * Creates a Data Access Object associated with the
     * database that created the given connection.
     *
     * @param connection the Database Connection object to
     *                   use when accessing the database
     */
    public EventDao(Connection connection) {
        super(connection);
    }

    /**
     * Creates a new event and adds it to the database.
     *
     * @param descendant Descendant of the new event
     * @param personID PersonID of the new event
     * @param latitude Latitude of the new event
     * @param longitude Longitude of the new event
     * @param country Country of the new event
     * @param city City of the new event
     * @param eventType EventType of the new event
     * @param year Year of the new event
     * @return the Event created
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public Event createEvent(String descendant, String personID, double latitude,
                            double longitude, String country, String city,
                            String eventType, int year)
            throws DatabaseException, InvalidValuesException {

        String eventID = UUID.randomUUID().toString();

        Event event = new Event(
                eventID,
                descendant,
                personID,
                latitude,
                longitude,
                country,
                city,
                eventType,
                year
        );

        addEvent(event);

        return event;
    }

    /**
     * Adds an event to the database.
     *
     * @param event the event to add to the database
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public void addEvent(Event event)
            throws DatabaseException, InvalidValuesException {

        if (event.getEventID() == null
                || event.getDescendant() == null
                || event.getPersonID() == null
                || event.getCountry() == null
                || event.getCity() == null
                || event.getEventType() == null) {
            throw new InvalidValuesException();
        }

        PreparedStatement stmt = null;
        try {

            String sql =
                    "insert into Events (" +
                            "EventID, Descendant, PersonID, Latitude, Longitude, " +
                            "Country, City, EventType, EventYear" +
                    ") " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Gets a specific event from the database.
     *
     * @param eventID the ID of the event desired
     * @return the Event object with the specified ID,
     *          or null if no such event exists
     * @throws DatabaseException if editing the database fails
     */
    public Event getEvent(String eventID)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Events " +
                    "where EventID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, eventID);

            rs = stmt.executeQuery();

            // if event was found, return model object
            if (rs.next()) {
                return new Event(
                        rs.getString(1), // EventID
                        rs.getString(2), // Descendant
                        rs.getString(3), // PersonID
                        rs.getDouble(4), // Latitude
                        rs.getDouble(5), // Longitude
                        rs.getString(6), // Country
                        rs.getString(7), // City
                        rs.getString(8), // Type
                        rs.getInt(9)     // Year
                );
            }
            // no event found
            return null;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Gets a specific event from the database.
     *
     * @param personID the ID of the person the event is associated with
     * @param eventType the type of event to look for
     * @return the Event object with the specified ID,
     *          or null if no such event exists
     * @throws DatabaseException if editing the database fails
     */
    public Event getEvent(String personID, String eventType)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Events " +
                    "where PersonID = ? and EventType = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, personID);
            stmt.setString(2, eventType);

            rs = stmt.executeQuery();

            // if event was found, return model object
            if (rs.next()) {
                return new Event(
                        rs.getString(1), // EventID
                        rs.getString(2), // Descendant
                        rs.getString(3), // PersonID
                        rs.getDouble(4), // Latitude
                        rs.getDouble(5), // Longitude
                        rs.getString(6), // Country
                        rs.getString(7), // City
                        rs.getString(8), // Type
                        rs.getInt(9)     // Year
                );
            }
            // no event found
            return null;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Gets all of the events from the database associated with the user's ancestors.
     *
     * @param userName the UserName of the descendant associated with the desired events
     * @return an array of Event objects with all events associated with the descendant specified,
     *          If no events are found, the array will be size 0.
     * @throws DatabaseException if editing the database fails
     */
    public Event[] getEvents(String userName)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Events " +
                    "where Descendant = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);

            rs = stmt.executeQuery();

            ArrayList<Event> events = new ArrayList<>();

            // if user was found, return model object
            while (rs.next()) {
                events.add(new Event(
                        rs.getString(1), // EventID
                        rs.getString(2), // Descendant
                        rs.getString(3), // PersonID
                        rs.getDouble(4), // Latitude
                        rs.getDouble(5), // Longitude
                        rs.getString(6), // Country
                        rs.getString(7), // City
                        rs.getString(8), // Type
                        rs.getInt(9)     // Year
                ));
            }

            Object[] array = events.toArray();
            return Arrays.copyOf(array, array.length, Event[].class);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Deletes all of the events of a specific user from the database.
     *
     * @param userName the UserName of the user
     * @throws DatabaseException if editing the database fails
     */
    public void deleteEvents(String userName)
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {
            String sql =
                    "delete from Events " +
                    "where Descendant = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }
}
