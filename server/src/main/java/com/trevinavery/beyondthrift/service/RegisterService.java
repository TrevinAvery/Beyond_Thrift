package com.trevinavery.beyondthrift.service;

import java.util.logging.Level;

import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.DatabaseException;
import com.trevinavery.beyondthrift.dao.InvalidValuesException;
import com.trevinavery.beyondthrift.dao.UserAlreadyExistsException;
import com.trevinavery.beyondthrift.dao.UserDao;
import com.trevinavery.beyondthrift.model.AuthToken;
import com.trevinavery.beyondthrift.model.User;
import com.trevinavery.beyondthrift.request.RegisterRequest;
import com.trevinavery.beyondthrift.result.RegisterResult;

/**
 * The RegisterService class uses a Database object to process a RegisterRequest and return the
 * RegisterResult.
 */
public class RegisterService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public RegisterService(Database database) {
        super(database);
    }

    /**
     * Registers a new user.
     *
     * @param request the RegisterRequest to fill
     * @return the RegisterResult object describing the result of the request
     */
    public RegisterResult perform(RegisterRequest request) {
        logger.info("Starting service: Register");

        setStatus(STATUS_BAD_REQUEST);

        boolean commit = false;

        try {
            database.open();

            UserDao userDao = database.getUserDao();

            User user = userDao.createUser(
                    request.getUserName(),
                    request.getPassword(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getGender()
            );

            // add 4 generations by default
            new FillService(database).fill(user.getUserName(), 4);

            // get the user from the database so it includes the user's PersonID
            user = userDao.getUser(user.getUserName());

            // log the user in
            AuthToken authToken = database.getAuthTokenDao().createAuthToken(user.getUserName());

            commit = true;

            logger.info("Success");
            setStatus(STATUS_SUCCESS);
            return new RegisterResult(
                    authToken.getAuthToken(),
                    user.getUserName(),
                    user.getPersonID()
            );

        } catch (InvalidValuesException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            return new RegisterResult("Request property missing or has invalid value");

        } catch (UserAlreadyExistsException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            return new RegisterResult("Username already taken by another user");

        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new RegisterResult("Internal server error");

        } finally {
            try {
                database.close(commit);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
