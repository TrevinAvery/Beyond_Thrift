package com.trevinavery.familymap.handler;

/*
/user/login
URL Path: /user/login
Description: Logs in the user and returns an auth token.
HTTP Method: POST
Auth Token Required: No
Request Body:
{
    "userName": "susan",        // Non-empty string
    "password": "mysecret"      // Non-empty string
}
Errors: Request property missing or has invalid value, Internal server error
Success Response Body:
{
    "authToken": "cf7a368f",    // Non-empty auth token string
    "userName": "susan",        // User name passed in with request
    "personId": "39f9fe46"      // Non-empty string containing the Person ID of the
                                // user’s generated Person object
}
5
Error Response Body:
{
    "message": "Description of the error"
}
*/

import java.io.IOException;

import com.trevinavery.familymap.request.LoginRequest;
import com.trevinavery.familymap.result.LoginResult;
import com.trevinavery.familymap.service.LoginService;

/**
 * The LoginHandler class is a handler for all login requests received by a server. It parses any
 * JSON received in the Request Body into a Request object, passes it to the appropriate Service
 * object, and then parses the returned Result object into JSON to return to the server as the
 * Response Body.
 */
public class LoginHandler extends AbstractHandler<LoginRequest, LoginResult> {

    @Override
    protected void handle() throws IOException {
        logger.info("Request received: Login");

        LoginRequest request = getRequest(LoginRequest.class);

        LoginService service = new LoginService(null);

        LoginResult result = service.perform(request);

        sendResult(result, service.getStatus());
    }
}
