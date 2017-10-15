package com.trevinavery.beyondthrift.proxy;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.trevinavery.beyondthrift.model.Donation;
import com.trevinavery.beyondthrift.model.DonationData;
import com.trevinavery.beyondthrift.request.LoginRequest;
import com.trevinavery.beyondthrift.request.RegisterRequest;
import com.trevinavery.beyondthrift.result.DonationResult;
import com.trevinavery.beyondthrift.result.EventResult;
import com.trevinavery.beyondthrift.result.LoginResult;
import com.trevinavery.beyondthrift.result.PersonResult;
import com.trevinavery.beyondthrift.result.RegisterResult;

/**
 * This class sends requests to the Family Map Server of an HTTP connection
 * and returns the server's response in the form of IResult objects.
 */
public class Proxy {

    private String domain;
    private int timeout;

    /**
     * Constructs a proxy object to send requests to the Family Map Server.
     *
     * @param host the domain, or IP address, of the server
     * @param port the port the server is running on
     */
    public Proxy(String host, String port) {
        domain = "http://" + host + ":" + port;
        timeout = 30000; // 30 seconds
    }

    /**
     * Registers a new user and logs them into the server. If an error occurs,
     * the description of the error can be retrieved with <code>getMessage()</code>.
     *
     * @param donation the RegisterRequest object to use while registering the new user
     * @return the result of the request
     */
    public DonationResult donate(Donation donation) {

        Gson gson = new Gson();

        String requestStr = gson.toJson(donation);

        InputStreamReader resultStr = post("/donate.php", requestStr);

        if (resultStr != null) {
            try {
                return gson.fromJson(resultStr, DonationResult.class);
            } catch (Exception e) {
                // probably a 404 or 500 server error
            }
        }

        return null;
    }

    public DonationData getDonations() {

        Gson gson = new Gson();

        InputStreamReader resultStr = post("/pickup.php", "");

        if (resultStr == null) {
            return null;
        }

        return gson.fromJson(resultStr, DonationData.class);
    }

    /**
     * Logs an existing user in the server. If an error occurs,
     * the description of the error can be retrieved with <code>getMessage()</code>.
     *
     * @param request the LoginRequest object to use while logging the user in
     * @return the result of the request
     */
    public LoginResult login(LoginRequest request) {

        Gson gson = new Gson();

        String requestStr = gson.toJson(request);

        InputStreamReader resultStr = post("/user/login/", requestStr);

        if (resultStr == null) {
            return null;
        }

        return gson.fromJson(resultStr, LoginResult.class);
    }

    /**
     * Gets all of the ancestors of the current in user.
     *
     * @param authToken the AuthToken for the logged in user
     * @return a PersonResult object containing an array of
     *          all ancestors of the current user
     */
    public PersonResult getPersons(String authToken) {

        Gson gson = new Gson();

        InputStreamReader resultStr = get("/person/", authToken);

        if (resultStr == null) {
            return null;
        }

        return gson.fromJson(resultStr, PersonResult.class);
    }

    /**
     * Gets all of the events of the current user and his ancestors.
     *
     * @param authToken the AuthToken for the current user
     * @return an EventResult object containing an array of
     *          all the events of the current user and his ancestors
     */
    public EventResult getEvents(String authToken) {

        Gson gson = new Gson();

        InputStreamReader resultStr = get("/event/", authToken);

        if (resultStr == null) {
            return null;
        }

        return gson.fromJson(resultStr, EventResult.class);
    }

    /**
     * Sends a post request to the server.
     *
     * @param path the path after the domain to send the request to
     * @param request the JSON representation of the request to send
     * @return an InputStreamReader for the response to the request
     */
    private InputStreamReader post(String path, String request) {

        try {
            URL url = new URL(domain + path);

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body
            http.addRequestProperty("Accept", "application/json");

            http.setConnectTimeout(timeout);
            http.setReadTimeout(timeout);

            http.connect();

            OutputStream reqBody = http.getOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(reqBody);
            os.write(request);
            os.close();
            reqBody.close();

            InputStream inputStream;
            if (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            return new InputStreamReader(inputStream);

        } catch (SocketTimeoutException e) {
            System.out.println("timeout");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends a get request to the server.
     *
     * @param path the path after the domain to send the request to
     * @param authToken the AuthToken of the current user
     * @return an InputStreamReader for the response to the request
     */
    private InputStreamReader get(String path, String authToken) {

        try {
            URL url = new URL(domain + path);

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false); // no request body
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.setConnectTimeout(timeout);
            http.setReadTimeout(timeout);

            http.connect();

            InputStream inputStream;
            if (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            return new InputStreamReader(inputStream);

        } catch (SocketTimeoutException e) {
            System.out.println("timeout");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
