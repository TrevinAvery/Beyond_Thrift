package com.trevinavery.familymap.service;

import java.util.logging.Level;

import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.DatabaseException;
import com.trevinavery.familymap.request.ClearRequest;
import com.trevinavery.familymap.result.ClearResult;

/**
 * The ClearService class uses a Database object to process a ClearRequest and return the
 * ClearResult.
 */
public class ClearService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public ClearService(Database database) {
        super(database);
    }

    /**
     * Clears the database.
     *
     * @param request a placeholder, does nothing
     * @return the ClearResult object describing the result of the request
     */
    public ClearResult perform(ClearRequest request) {
        logger.info("Starting service: Clear");

        boolean commit = false;

        try {
            database.open();
            database.clear();
            commit = true;

            String resultStr = "Clear succeeded.";
            logger.info(resultStr);

            setStatus(STATUS_SUCCESS);
            return new ClearResult(resultStr);

        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new ClearResult("Internal server error");
        } finally {
            try {
                database.close(commit);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }

    }
}
