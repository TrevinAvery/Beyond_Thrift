package com.trevinavery.beyondthrift.handler;

/*
/event
URL Path: /event
Description: Returns ALL events for ALL family members of the current user. The current
user is determined from the provided auth token.
HTTP Method: GET
Auth Token Required: Yes
Request Body: None
Errors: Invalid auth token, Internal server error
Success Response Body: The response body returns a JSON object with a "data" attribute that
contains an array of Event objects. Each Event object has the same format as described in
previous section on the /event/[eventID] API.
{
    "data": [  Array of Event objects  ]
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

/*
/event/[eventID]
URL Path: /event/[eventID]
Example: /event/251837d7
Description: Returns the single Event object with the specified ID.
HTTP Method: GET
Auth Token Required: Yes
Request Body: None
8
Errors: Invalid auth token, Invalid eventID parameter, Requested event does not belong to this
user, Internal server error
Success Response Body:
{
"descendant": "susan" // Name of user account this event belongs to (non-empty
// string)
"eventID": "251837d7", // Event’s unique ID (non-empty string)
"personID": "7255e93e", // ID of the person this event belongs to (non-empty string)
"latitude": 65.6833, // Latitude of the event’s location (number)
"longitude": -17.9, // Longitude of the event’s location (number)
"country": "Iceland", // Name of country where event occurred (non-empty
// string)
"city": "Akureyri", // Name of city where event occurred (non-empty string)
"eventType": "birth", // Type of event ("birth", "baptism", etc.) (non-empty string)
"year": "1912", // Year the event occurred (integer formatted as string)
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

import com.trevinavery.beyondthrift.request.EventRequest;
import com.trevinavery.beyondthrift.result.EventResult;
import com.trevinavery.beyondthrift.service.EventService;

/**
 * The EventHandler class is a handler for all event requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class EventHandler extends AbstractHandler {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Event");

        String eventID = null;

        // get parameters from uri
        String[] params = getParams();
        if (params.length != 0) {
            eventID = params[0];
        }

        EventRequest request = new EventRequest(
                getAuthToken(),
                eventID
        );

        EventService service = new EventService(null);

        EventResult result = service.perform(request);

        // if success and request was for a single event
        if (service.getStatus() == EventService.STATUS_SUCCESS
                && eventID != null) {
            Gson gson = new Gson();

            String response = gson.toJson(result.getData()[0]);

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json");

            exchange.sendResponseHeaders(EventService.STATUS_SUCCESS, 0);

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
