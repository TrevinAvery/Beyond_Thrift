package com.trevinavery.beyondthrift.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a dafault handler to accept all requests that do not have a
 * specific associated API. This will serve up all public web files.
 */
public class DefaultHandler implements HttpHandler {

    protected static Logger logger;

    static {
        logger = Logger.getLogger("familymapserver");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            String dir = "web";
            String uri = exchange.getRequestURI().toString();

            // default to index.html
            if (uri.equals("/")) {
                uri = "/index.html";
            }

            Path path = Paths.get(dir + uri);


            if (Files.exists(path)) {

                Headers headers = exchange.getResponseHeaders();

                String type = null;

                switch (uri.substring(uri.lastIndexOf(".") + 1)) {
                    case "html":
                    case "htm":
                        type = "text/html";
                        break;
                    case "css":
                        type = "text/css";
                        break;
                    case "js":
                        type = "text/javascript";
                        break;
                    case "gif":
                        type = "image/gif";
                        break;
                    case "jpeg":
                    case "jpg":
                        type = "image/jpeg";
                        break;
                    case "png":
                        type = "image/gif";
                        break;
                    default:
                        type = "text/plain";
                        break;
                }

                headers.set("Content-Type", type);

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                Files.copy(path, respBody);
                respBody.close();

                success = true;
            }

            if (!success) {

                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type","text/html");

                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                path = Paths.get(dir + "/HTML/404.html");

                OutputStream respBody = exchange.getResponseBody();
                Files.copy(path, respBody);
                respBody.close();

                exchange.getResponseBody().close();
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
        }
    }
}
