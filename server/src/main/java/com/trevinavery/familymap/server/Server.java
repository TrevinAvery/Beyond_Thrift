package com.trevinavery.familymap.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.trevinavery.familymap.handler.ClearHandler;
import com.trevinavery.familymap.handler.DefaultHandler;
import com.trevinavery.familymap.handler.EventHandler;
import com.trevinavery.familymap.handler.FillHandler;
import com.trevinavery.familymap.handler.LoadHandler;
import com.trevinavery.familymap.handler.LoginHandler;
import com.trevinavery.familymap.handler.PersonHandler;
import com.trevinavery.familymap.handler.RegisterHandler;

/**
 * The Server class is the main class for the Family Map Server. This class creates
 * an HTTP server to receive requests from a client for logging in, generating, and
 * retrieving family history data. This data is either generated randomly, or loaded
 * in from a JSON file.
 *
 * When the server runs, all command-line arguments are passed in to Server.main.
 * For this server, the only command-line argument is the port number on which
 * the server should accept incoming client connections.
 */
public class Server {

    private static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("familymapserver");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        // log to console
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        // log to file
        FileHandler fileHandler = new FileHandler(
                "logs" + File.separator + "log.txt", false);
        fileHandler.setLevel(logLevel);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }


    // The maximum number of waiting incoming connections to queue.
    private static final int MAX_WAITING_CONNECTIONS = 12;

    private static long authTokenTimeout = 3600000; // one hour

    public static long getAuthTokenTimeout() {
        return authTokenTimeout;
    }

    /**
     * This initializes and runs the server.
     *
     * @param portNumber the port number for the server to receive connections
     * @param authTokenTimeout how long an AuthToken is valid after creation
     */
    private void run(int portNumber, int authTokenTimeout) {

        // convert authTokenTimeout to milliseconds
        Server.authTokenTimeout = (authTokenTimeout * 1000);

        logger.info("Initializing HTTP Server");

        HttpServer server;

        try {
            server = HttpServer.create(
                    new InetSocketAddress(portNumber),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor"
        server.setExecutor(null);

        logger.info("Creating contexts");

        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/", new DefaultHandler());

        logger.info("Starting server on port " + portNumber);
        server.start();

        try {
            String ip = InetAddress.getLocalHost().toString();
            logger.info("Server started. IP = "
                    + ip.substring(ip.indexOf("/") + 1) + ":" + portNumber);
        } catch (UnknownHostException e) {
            logger.info("Server started. Could not retrieve IP address.");
        }
    }

    /**
     * The main method that runs the Family Map Server program.
     *
     * USAGE: Server <port-number> <auth-token-timeout>
     *
     * @param args array of commandline arguments
     */
    public static void main(String[] args) {
        try {
            int portNumber = Integer.parseInt(args[0]);
            int authTokenTimeout = Integer.parseInt(args[1]);
            new Server().run(portNumber, authTokenTimeout);
        } catch (NumberFormatException e) {
            System.err.println("The parameters entered could not be read as numbers.");
            System.err.println("USAGE: Server <port-number> <auth-token-timeout>");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Some parameters were not entered.");
            System.err.println("USAGE: Server <port-number> <auth-token-timeout>");
        }
    }
}
