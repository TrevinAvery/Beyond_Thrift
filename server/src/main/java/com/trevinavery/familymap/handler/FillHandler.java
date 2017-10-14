package com.trevinavery.familymap.handler;

/*
/fill/[username]/{generations}
URL Path: /fill/[username]/{generations}
Example: /fill/susan/3
Description: Populates the server's database with generated data for the specified user name.
The required "username" parameter must be a user already registered with the server. If there is
any data in the database already associated with the given user name, it is deleted. The
optional "generations" parameter lets the caller specify the number of generations of ancestors
to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
persons each with associated events).
HTTP Method: POST
Auth Token Required: No
Request Body: None
Errors: Invalid username or generations parameter, Internal server error
Success Response Body:
{
    "message": "Successfully added X persons and Y events to the database."
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

import java.io.IOException;

import com.trevinavery.familymap.request.FillRequest;
import com.trevinavery.familymap.result.FillResult;
import com.trevinavery.familymap.service.FillService;

/**
 * The FillHandler class is a handler for all fill requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class FillHandler extends AbstractHandler {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Fill");

        String userName = null;
        int numOfGenerations;

        // get parameters from uri
        String[] params = getParams();
        if (params.length != 0) {
            userName = params[0];
        }

        if (params.length == 2) {
            try {
                numOfGenerations = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                numOfGenerations = -1; // set to -1 to send back error
            }
        } else {
            numOfGenerations = 4; // default
        }

        FillRequest request = new FillRequest(
                userName,
                numOfGenerations
        );

        FillService service = new FillService(null);
        FillResult result = service.perform(request);

        sendResult(result, service.getStatus());
    }
}
