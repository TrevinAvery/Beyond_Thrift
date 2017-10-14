package com.trevinavery.familymap.proxy;

import com.trevinavery.familymap.request.LoginRequest;
import com.trevinavery.familymap.request.RegisterRequest;
import com.trevinavery.familymap.result.EventResult;
import com.trevinavery.familymap.result.LoginResult;
import com.trevinavery.familymap.result.PersonResult;
import com.trevinavery.familymap.result.RegisterResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 6/12/17.
 */
public class ProxyTest {

    private String host = "192.168.2.46";
    private String port = "1234";

    private Proxy proxy = new Proxy(host, port);

    private String username = "username";
    private String password = "password";
    private String email = "email";
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String gender = "m";

    private String authToken = "authToken";
    private String personID = "personID";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void register() throws Exception {
        RegisterRequest request = new RegisterRequest(
                username,
                password,
                email,
                firstName,
                lastName,
                gender
        );

        RegisterResult result = proxy.register(request);

        assertNotNull(result);

        if (result.getMessage() == null) {
            assertNotNull(result.getAuthToken());
            assertNotNull(result.getPersonID());
            assertEquals(username, result.getUserName());
        } else {
            assertNull(result.getAuthToken());
            assertNull(result.getUserName());
            assertNull(result.getPersonID());
        }
    }

    @Test
    public void login() throws Exception {
        LoginRequest request = new LoginRequest(
                username,
                password
        );

        LoginResult result = proxy.login(request);

        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());

        assertEquals(username, result.getUserName());

        assertNull(result.getMessage());
    }

    @Test
    public void loginBadInfo() throws Exception {
        LoginRequest request = new LoginRequest(
                "this is not a real username",
                "this is not a real password"
        );

        LoginResult result = proxy.login(request);

        assertNotNull(result);
        assertNotNull(result.getMessage());

        assertNull(result.getAuthToken());
        assertNull(result.getUserName());
        assertNull(result.getPersonID());
    }

    @Test
    public void getPersons() throws Exception {
        LoginRequest request = new LoginRequest(
                username,
                password
        );

        LoginResult loginResult = proxy.login(request);

        assertNotNull(loginResult);

        PersonResult personResult = proxy.getPersons(loginResult.getAuthToken());

        assertNotNull(personResult);
        assertNotNull(personResult.getData());
    }

    @Test
    public void getEvents() throws Exception {
        LoginRequest request = new LoginRequest(
                username,
                password
        );

        LoginResult loginResult = proxy.login(request);

        assertNotNull(loginResult);

        EventResult eventResult = proxy.getEvents(loginResult.getAuthToken());

        assertNotNull(eventResult);
        assertNotNull(eventResult.getData());
    }

}