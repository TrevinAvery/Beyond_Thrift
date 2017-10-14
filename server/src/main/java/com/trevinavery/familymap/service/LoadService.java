package com.trevinavery.familymap.service;

import java.util.logging.Level;

import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.DatabaseException;
import com.trevinavery.familymap.dao.EventDao;
import com.trevinavery.familymap.dao.InvalidValuesException;
import com.trevinavery.familymap.dao.PersonDao;
import com.trevinavery.familymap.dao.UserAlreadyExistsException;
import com.trevinavery.familymap.dao.UserDao;
import com.trevinavery.familymap.model.Event;
import com.trevinavery.familymap.model.Person;
import com.trevinavery.familymap.model.User;
import com.trevinavery.familymap.request.LoadRequest;
import com.trevinavery.familymap.result.LoadResult;

/**
 * The LoadService class uses a Database object to process a LoadRequest and return the
 * LoadResult.
 */
public class LoadService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public LoadService(Database database) {
        super(database);
    }

    /**
     * Loads a large set of data into the database.
     *
     * @param request the LoadRequest to fill
     * @return the LoadResult object describing the result of the request
     */
    public LoadResult perform(LoadRequest request) {
        logger.info("Starting service: Load");

        setStatus(STATUS_BAD_REQUEST);

        boolean commit = false;

        try {
            database.open();

            database.clear();

            int usersAdded = 0;
            int personsAdded = 0;
            int eventsAdded = 0;

            User[] users = request.getUsers();
            if (users != null) {
                UserDao userDao = database.getUserDao();

                for (User user : users) {
                    userDao.addUser(user);
                    ++usersAdded;
                }
            }

            Person[] persons = request.getPersons();
            if (persons != null) {
                PersonDao personDao = database.getPersonDao();

                for (Person person : persons) {
                    personDao.addPerson(person);
                    ++personsAdded;
                }
            }

            Event[] events = request.getEvents();
            if (events != null) {
                EventDao eventDao = database.getEventDao();

                for (Event event : events) {
                    eventDao.addEvent(event);
                    ++eventsAdded;
                }
            }

            commit = true;

            setStatus(STATUS_SUCCESS);
            String resultStr = "Successfully added " +
                    usersAdded + " users, " +
                    personsAdded + " persons, and " +
                    eventsAdded + " events to the database.";

            logger.info(resultStr);
            return new LoadResult(resultStr);
        } catch (UserAlreadyExistsException e) {
            logger.log(Level.WARNING,
                    "User already exists: " + e.getMessage(), e);
            return new LoadResult("Attempted to enter a user that already exists");

        } catch (InvalidValuesException e) {
            logger.log(Level.WARNING, "Invalid values: " + e.getMessage(), e);
            return new LoadResult("Invalid values were included in the load data");

        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new LoadResult("Internal server error");

        } finally {
            try {
                database.close(commit);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }

    }
}
