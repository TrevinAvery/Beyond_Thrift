package com.trevinavery.familymap.handler;

/*
/user/register
URL Path: /user/register
Description: Creates a new user account, generates 4 generations of ancestor data for the new user,
logs the user in, and returns an auth token.
HTTP Method: POST
Auth Token Required: No
Request Body:
{
    "userName": "susan",        // Non-empty string
    "password": "mysecret",     // Non-empty string
    "email": "susan@gmail.com", // Non-empty string
    "firstName": "Susan",       // Non-empty string
    "lastName": "Ellis",        // Non-empty string
    "gender": "f"               // "f" or "m"
}
Errors: Request property missing or has invalid value, Username already taken by another user,
Internal server error
Success Response Body:
{
    "authToken": "cf7a368f",    // Non-empty auth token string
    "userName": "susan",        // User name passed in with request
    "personId": "39f9fe46"      // Non-empty string containing the Person ID of the
                                // user’s generated Person object
}
Error Response Body:
{
    "message": "Description of the error"
}
*/

import java.io.IOException;

import com.trevinavery.familymap.request.RegisterRequest;
import com.trevinavery.familymap.result.RegisterResult;
import com.trevinavery.familymap.service.RegisterService;


/**
 * The RegisterHandler class is a handler for all register requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class RegisterHandler extends AbstractHandler<RegisterRequest, RegisterResult> {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Register");

        RegisterRequest request = getRequest(RegisterRequest.class);

        RegisterService service = new RegisterService(null);

        RegisterResult result = service.perform(request);

        sendResult(result, service.getStatus());
    }
}
