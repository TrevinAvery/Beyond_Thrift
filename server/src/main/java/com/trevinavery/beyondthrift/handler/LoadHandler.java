package com.trevinavery.beyondthrift.handler;

/*
/load
URL Path: /load
Description: Clears all data from the database (just like the /clear API), and then loads the
posted user, person, and event data into the database.
HTTP Method: POST
Auth Token Required: No
Request Body: The "users" property in the request body contains an array of users to be
created. The "persons" and "events" properties contain family history information for these
users. The objects contained in the "persons" and "events" arrays should be added to the
server’s database. The objects in the "users" array have the same format as those passed to
the /user/register API with the addition of the personID. The objects in the "persons" array have
the same format as those returned by the /person/[personID] API. The objects in the "events"
array have the same format as those returned by the /event/[eventID] API.
{
    "users": [  Array of User objects  ],
    "persons": [  Array of Person objects  ],
    "events": [  Array of Event objects  ]
}
Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
Success Response Body:
{
    "message": "Successfully added X users, Y persons, and Z events to the database."
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

import java.io.IOException;

import com.trevinavery.beyondthrift.request.LoadRequest;
import com.trevinavery.beyondthrift.result.LoadResult;
import com.trevinavery.beyondthrift.service.LoadService;

/**
 * The LoadHandler class is a handler for all load requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class LoadHandler extends AbstractHandler<LoadRequest, LoadResult> {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Load");

        LoadRequest request = getRequest(LoadRequest.class);

        LoadService service = new LoadService(null);

        LoadResult result = service.perform(request);

        sendResult(result, service.getStatus());
    }
}
