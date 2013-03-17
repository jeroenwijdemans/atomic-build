package atomic.server.plugins;

import atomic.server.domain.AlarmPhase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketPlugin implements Plugin {

    private static final Logger LOGGER = Logger.getLogger(SocketPlugin.class.getName());

    private enum Message {
        RAISE,
        STAND_DOWN,
        ESCALATE
    }

    private volatile Message lastMessage;
    private SocketServer socketServer;

    public SocketPlugin() {
        try {
            socketServer = new SocketServer();
            Executors.newFixedThreadPool(1).execute(socketServer);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to start SocketPlugin");
        }
    }

    @Override
    public void prepareActionOn() {
        if (lastMessage != null) {
            LOGGER.info("Going to: " + lastMessage);
        }

    }

    @Override
    public void takeActionOn(AlarmPhase alarmPhase) {
        if (lastMessage == null) {
            return;
        }

        switch (lastMessage) {
            case RAISE:
                alarmPhase.increase();
                break;
            case ESCALATE:
                alarmPhase.escalate();
                break;
            case STAND_DOWN:
                alarmPhase.standDown();
                break;
            default:
                LOGGER.severe(String.format("Unknown Message %s... cannot take action!", lastMessage));
        }
        lastMessage = null;
    }

    @Override
    public void shutdown() {
        socketServer.stopRunning();
    }

    class SocketServer implements Runnable {

        private ServerSocket serverSocket;
        private Socket clientSocket = null;
        private volatile boolean running = true;

        public SocketServer() throws IOException {
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

                Message result = parseMessageLine(line);
                if (result != null) {
                    lastMessage = result;
                }

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

        private Message parseMessageLine(String line) {
            if (line == null) {
                LOGGER.info("Empty or multi line message not supported");
                return null;
            }

            line = line.toUpperCase();
            if (line.equals("RAISE")) {
                return Message.RAISE;
            } else if (line.equals("STAND_DOWN") || line.equals("STANDDOWN")) {
                return Message.STAND_DOWN;
            } else if (line.equals("ESCALATE")) {
                return Message.ESCALATE;
            } else {
                LOGGER.warning("Ignoring unknown message arrived : " + line);
            }
            return null;
        }

        public void stopRunning() {
            running = false;
        }
    }
}
