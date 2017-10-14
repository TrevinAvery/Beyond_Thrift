package com.trevinavery.familymap.handler;

/*
/clear
URL Path: /clear
Description: Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
HTTP Method: POST
Auth Token Required: No
Request Body: None
Errors: Internal server error
Success Response Body:
{
    "message": "Clear succeeded."
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

import java.io.IOException;

import com.trevinavery.familymap.request.ClearRequest;
import com.trevinavery.familymap.result.ClearResult;
import com.trevinavery.familymap.service.ClearService;

/**
 * The ClearHandler class is a handler for all clear requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class ClearHandler extends AbstractHandler<ClearRequest, ClearResult> {

    @Override
    protected void handle() throws IOException {

        ClearService service = new ClearService(null);

        ClearResult result = service.perform(new ClearRequest());

        sendResult(result, service.getStatus());
    }
}
