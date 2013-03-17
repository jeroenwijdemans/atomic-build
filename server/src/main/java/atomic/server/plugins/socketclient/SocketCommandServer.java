package atomic.server.plugins.socketclient;

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
    private volatile SocketMessage lastMessage = null;
    private volatile boolean running = true;

    public SocketCommandServer() throws IOException {
        serverSocket = new ServerSocket(4444);
    }

    @Override
    public void run() {
        while (running) {
            acceptIncomingCalls();
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptIncomingCalls() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // wait for client to tell something
            clientSocket = serverSocket.accept();
            LOGGER.fine("Incoming message");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String inputLine, outputLine;

            String line = in.readLine();

            lastMessage = messageParser.parseMessageLine(line);

//                while ((inputLine = ) != null) {
//                    message = inputLine;
//                    if (outputLine.equals("Bye."))
//                        break;
//                }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void stopRunning() {
        running = false;
    }

    public SocketMessage takeMessage() {
        SocketMessage tmpSocketMessage  = this.lastMessage;
        this.lastMessage = null;
        return tmpSocketMessage;
    }

    public SocketMessage getLastMessage() {
        return lastMessage;
    }
}