import atomic.server.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * User: jeroenwijdemans
 * Date: 4/1/13
 */
public class ClientListener implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientListener.class.getName());
    private final static int DEFAULT_SERVER_PORT = 60001;

    public final static String HOST = "localhost";
    public final static int PORT = 54321;
    public final int SERVER_PORT;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private ServerSocket serverSocket;

    private volatile boolean running = true;
    private final UITextWriter uiTextWriter;
    private final UILedLight uiLedLight;

    // TODO find a nice way to abstract Text & Client
    public ClientListener(UITextWriter uiTextWriter, UILedLight uiLedLight) {
        this.uiTextWriter = uiTextWriter;
        this.uiLedLight = uiLedLight;
        int port = DEFAULT_SERVER_PORT;
        boolean isStarted = false;
        while (!isStarted) {
            try {
                serverSocket = new ServerSocket(++port);
                isStarted = true;
            } catch (IOException e) {
                LOGGER.severe("Failed to start SocketPlugin on port" + port);
            }
        }
        SERVER_PORT = port;
        writeTextOnScreen(String.format("Started listing on port %d", SERVER_PORT));
        executor.execute(this);
    }

    private void writeTextOnScreen(String text) {
        uiTextWriter.writeText(text);
    }

    @Override
    public void run() {
        registerOnServer();
        while (running) {
            LOGGER.info("Start listing on " + SERVER_PORT);
            listeningOnServerSocket();
        }
    }

    private void listeningOnServerSocket() {
        PrintWriter out = null;
        BufferedReader in = null;
        Socket clientSocket = null;

        try {
            // wait for client to tell something
            clientSocket = serverSocket.accept();
            LOGGER.info("Incoming message");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String inputLine, outputLine;

            String line = in.readLine();

//            writeTextOnScreen(String.format("Got call %s", line));
            uiLedLight.enableLight(line);

        } catch (IOException e) {
            LOGGER.info("Error while accepting incoming call.");
            e.printStackTrace();
        } finally {
            Util.closeQuietly(out);
            Util.closeQuietly(in);
            Util.closeQuietly(clientSocket);
        }
    }

    private void registerOnServer() {
        sendRequest("register " + SERVER_PORT);
    }

    private void unregisterOnServer() {
        sendRequest("unregister");
    }

    private void sendRequest(String request) {
        writeTextOnScreen(String.format("Sending '%s' to server.", request));
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.write(request);
            writeTextOnScreen(String.format("Send registration request"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(out);
            Util.closeQuietly(socket);
        }
        writeTextOnScreen("Done sending Send registration request");
    }

    public void stop() {
        LOGGER.info("Stopping client");
        running = false;
        // send de-register signal
        unregisterOnServer();
        // stop own servers
        Util.closeQuietly(serverSocket);

        LOGGER.info("Client stopped");
    }
}
