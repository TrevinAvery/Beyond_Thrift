package com.trevinavery.familymap.handler;

/*
/person
URL Path: /person
Description: Returns ALL family members of the current user. The current user is
determined from the provided auth token.
HTTP Method: GET
Auth Token Required: Yes
Request Body: None
Errors: Invalid auth token, Internal server error
Success Response Body: The response body returns a JSON object with a "data" attribute that
contains an array of Person objects. Each Person object has the same format as described in
previous section on the /person/[personID] API.
{
    "data": [  Array of Person objects  ]
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

/*
/person/[personID]
URL Path: /person/[personID]
Example: /person/7255e93e
Description: Returns the single Person object with the specified ID.
HTTP Method: GET
Auth Token Required: Yes
Request Body: None
Errors: Invalid auth token, Invalid personID parameter, Requested person does not belong to
this user, Internal server error
Success Response Body:
{
    "descendant": "susan", // Name of user account this person belongs to
    "personID": "7255e93e", // Person’s unique ID
    "firstName": "Stuart", // Person’s first name
    "lastName": "Klocke", // Person’s last name
    "gender": "m", // Person’s gender ("m" or "f")
    "father": "7255e93e" // ID of person’s father [OPTIONAL, can be missing]
    "mother": "f42126c8" // ID of person’s mother [OPTIONAL, can be missing]
    "spouse":"f42126c8" // ID of person’s spouse [OPTIONAL, can be missing]
}
Error Response Body:
{
    "message": "Description of the error"
}
 */


import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.trevinavery.familymap.request.PersonRequest;
import com.trevinavery.familymap.result.PersonResult;
import com.trevinavery.familymap.service.PersonService;

/**
 * The PersonHandler class is a handler for all person requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class PersonHandler extends AbstractHandler<PersonRequest, PersonResult> {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Person");

        String personID = null;

        // get parameters from uri
        String[] params = getParams();
        if (params.length != 0) {
            personID = params[0];
        }

        PersonRequest request = new PersonRequest(
                getAuthToken(),
                personID
        );

        PersonService service = new PersonService(null);

        PersonResult result = service.perform(request);

        // if success and request was for an individual
        if (service.getStatus() == PersonService.STATUS_SUCCESS
                && personID != null) {
            Gson gson = new Gson();

            String response = gson.toJson(result.getData()[0]);

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json");

            exchange.sendResponseHeaders(PersonService.STATUS_SUCCESS, 0);

            OutputStream respBody = exchange.getResponseBody();
            OutputStreamWriter os = new OutputStreamWriter(respBody);
            os.write(response);
            os.close();
            respBody.close();
        } else {
            sendResult(result, service.getStatus());
        }
    }
}
