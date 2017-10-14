package com.trevinavery.beyondthrift.service;

import java.util.logging.Level;

import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.DatabaseException;
import com.trevinavery.beyondthrift.dao.PersonDao;
import com.trevinavery.beyondthrift.model.Person;
import com.trevinavery.beyondthrift.request.PersonRequest;
import com.trevinavery.beyondthrift.result.PersonResult;

/**
 * The PersonService class uses a Database object to process a PersonRequest and return the
 * PersonResult.
 */
public class PersonService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public PersonService(Database database) {
        super(database);
    }

    /**
     * Gets either a specified person, or all of the ancestors of the
     * current user from the database.
     *
     * @param request a PersonRequest object containing all required information
     * @return the result of the request
     */
    public PersonResult perform(PersonRequest request) {
        logger.info("Starting service: Person");

        setStatus(STATUS_BAD_REQUEST);

        try {
            database.open();

            // authenticate
            String userName = authenticate(request.getAuthToken());

            if (userName == null) {
                return new PersonResult("Invalid auth token");
            }

            PersonDao personDao = database.getPersonDao();

            // determine type of request and return result
            if (request.getPersonID() != null) {
                // individual
                logger.info("PersonService: Get Individual");

                Person person = personDao.getPerson(request.getPersonID());

                if (person == null) {
                    return new PersonResult("Invalid personID parameter");

                } else if (!userName.equals(person.getDescendant())) {
                    return new PersonResult("Requested person does not belong to this user");
                }

                setStatus(STATUS_SUCCESS);
                return new PersonResult(person);
            } else {
                // ancestors
                logger.info("PersonService: Get Ancestors");

                Person[] ancestors = personDao.getAncestors(userName);

                setStatus(STATUS_SUCCESS);
                return new PersonResult(ancestors);
            }
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new PersonResult("Internal server error");

        } finally {
            try {
                database.close(false);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
