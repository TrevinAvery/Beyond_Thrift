package com.trevinavery.familymap.service;

import java.util.logging.Level;

import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.DatabaseException;
import com.trevinavery.familymap.dao.UserDao;
import com.trevinavery.familymap.model.AuthToken;
import com.trevinavery.familymap.model.User;
import com.trevinavery.familymap.request.LoginRequest;
import com.trevinavery.familymap.result.LoginResult;


/**
 * The LoginService class uses a Database object to process a LoginRequest and return the
 * LoginResult.
 */
public class LoginService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public LoginService(Database database) {
        super(database);
    }

    /**
     * Logs a user in.
     *
     * @param request the LoginRequest to fill
     * @return the LoginResult object describing the result of the request
     */
    public LoginResult perform(LoginRequest request) {
        logger.info("Starting service: Login");

        setStatus(STATUS_BAD_REQUEST);

        boolean commit = false;

        try {
            database.open();

            UserDao userDao = database.getUserDao();
            User user = userDao.getUser(request.getUserName());

            // if user is not null, then the userName matched
            // check if the password matches
            if (user != null && user.getPassword().equals(request.getPassword())) {
                // password matched, create authToken and log the user in
                AuthToken authToken = database.getAuthTokenDao().createAuthToken(user.getUserName());

                commit = true;

                logger.info("Success");
                setStatus(STATUS_SUCCESS);
                return new LoginResult(
                        authToken.getAuthToken(),
                        user.getUserName(),
                        user.getPersonID()
                );
            } else {
                String resultStr = "Request property missing or has invalid value";
                logger.info(resultStr);
                return new LoginResult(resultStr);
            }
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new LoginResult("Internal server error");

        } finally {
            try {
                database.close(commit);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
