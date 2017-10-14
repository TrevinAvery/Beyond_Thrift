package com.trevinavery.familymap.service;

import java.util.logging.Level;

import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.DatabaseException;
import com.trevinavery.familymap.dao.EventDao;
import com.trevinavery.familymap.model.Event;
import com.trevinavery.familymap.request.EventRequest;
import com.trevinavery.familymap.result.EventResult;

/**
 * The EventService class uses a Database object to process an EventRequest and return the
 * EventResult.
 */
public class EventService extends AbstractService {

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public EventService(Database database) {
        super(database);
    }

    /**
     * Gets either a specified event, or all of the events of the
     * current user from the database.
     *
     * @param request an EventRequest object containing all required information
     * @return the result of the request
     */
    public EventResult perform(EventRequest request) {
        logger.info("Starting service: Event");

        setStatus(STATUS_BAD_REQUEST);

        try {
            database.open();

            // authenticate
            String userName = authenticate(request.getAuthToken());

            if (userName == null) {
                return new EventResult("Invalid auth token");
            }

            EventDao eventDao = database.getEventDao();

            if (request.getEventID() != null) {
                // specific event
                logger.info("EventService: Get Single Event");

                Event event = eventDao.getEvent(request.getEventID());

                if (event == null) {
                    return new EventResult("Invalid eventID parameter");

                } else if (!userName.equals(event.getDescendant())) {
                    return new EventResult("Requested event does not belong to this user");
                }

                setStatus(STATUS_SUCCESS);
                return new EventResult(event);
            } else {
                // all events
                logger.info("EventService: Get All Events");

                Event[] events = eventDao.getEvents(userName);

                setStatus(STATUS_SUCCESS);
                return new EventResult(events);
            }
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new EventResult("Internal server error");
        } finally {
            try {
                database.close(false);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
