package com.trevinavery.familymap.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import com.trevinavery.familymap.request.IRequest;
import com.trevinavery.familymap.result.IResult;

/**
 * An abstract class to share like code for all services.
 *
 * @param <Request> a class that implements IRequest
 *                 to be used in storing the JSON request as a Java object
 * @param <Result>  a class that implements IResult
 *                 to be used in creating the JSON response
 */
public abstract class AbstractHandler<Request extends IRequest, Result extends IResult>
        implements HttpHandler {

    protected static Logger logger;

    static {
        logger = Logger.getLogger("familymapserver");
    }

    protected HttpExchange exchange;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
        handle();
    }

    /**
     * Handle incoming requests and send the appropriate response.
     *
     * @throws IOException if reading request or sending response fails
     */
    protected abstract void handle() throws IOException;

    /**
     * Retrieves the JSON request and converts it into a usable Java object.
     *
     * @param cls the Request class to read the request as
     * @return the request as a subclass of IRequest
     */
    protected Request getRequest(Class<? extends Request> cls) {
        Gson gson = new Gson();

        InputStreamReader jsonStr = new InputStreamReader(exchange.getRequestBody());

        return gson.fromJson(jsonStr, cls);
    }

    /**
     * Converts the IResult object into JSON and sends it as the exchange response.
     *
     * @param result the IResult object to convert and send
     * @param status the status response header to use
     * @throws IOException if something goes wrong
     */
    protected void sendResult(IResult result, int status) throws IOException {
        Gson gson = new Gson();

        String response = gson.toJson(result);

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");

        exchange.sendResponseHeaders(status, 0);

        OutputStream respBody = exchange.getResponseBody();
        OutputStreamWriter os = new OutputStreamWriter(respBody);
        os.write(response);
        os.close();
        respBody.close();
    }

    /**
     * Returns the parameters (if any) from the URI.
     *
     * @return the parameters in the URI
     */
    protected String[] getParams() {
        // get parameters from uri
        String uri = exchange.getRequestURI().toString();
        // URI format should be "/<request>/<param1>[/<param2>]
        // skip to after the second slash
        String paramString = uri.substring(uri.indexOf("/", 1) + 1);
        if (paramString.length() == 0) {
            return new String[0];
        } else {
            return paramString.split("/");
        }
    }

    /**
     * Gets the AuthToken string from the request headers.
     *
     * @return the AuthToken string
     */
    protected String getAuthToken() {
        Headers reqHeaders = exchange.getRequestHeaders();
        return reqHeaders.getFirst("Authorization");
    }
}
