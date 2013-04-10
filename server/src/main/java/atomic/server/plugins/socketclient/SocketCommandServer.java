package atomic.server.plugins.socketclient;

import atomic.server.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketCommandServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SocketCommandServer.class.getName());

    private final ServerSocket serverSocket;
    private final MessageParser messageParser = MessageParser.getInstance();

    private Socket clientSocket = null;
    private volatile SocketMessage lastMessage = SocketMessage.NO_MESSAGE;
    private volatile boolean running = true;

    public SocketCommandServer() {
        LOGGER.info("Creating SocketCommandServer");
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            LOGGER.severe("Failed to start SocketPlugin");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Start accepting incoming calls...");
        while (running) {
            acceptIncomingCalls();
        }
        Util.closeQuietly(clientSocket);
        LOGGER.info("Stopping Socket Plugin ");
    }

    private void acceptIncomingCalls() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // wait for client to tell something
            clientSocket = serverSocket.accept();
            LOGGER.info("Incoming message");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String inputLine, outputLine;

            String line = in.readLine();

            LOGGER.info("Taking message " + line);

            lastMessage = messageParser.parseMessageLine(line);

//                while ((inputLine = ) != null) {
//                    message = inputLine;
//                    if (outputLine.equals("Bye."))
//                        break;
//                }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(out);
            Util.closeQuietly(in);
        }
    }

    public void stopRunning() {
        running = false;
    }

    public SocketMessage takeMessage() {
        SocketMessage tmpSocketMessage = this.lastMessage;
        this.lastMessage = SocketMessage.NO_MESSAGE;
        return tmpSocketMessage;
    }

    public SocketMessage getLastMessage() {
        return lastMessage;
    }
}