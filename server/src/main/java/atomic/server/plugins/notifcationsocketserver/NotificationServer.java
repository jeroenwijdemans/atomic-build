package atomic.server.plugins.notifcationsocketserver;

import atomic.server.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class NotificationServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(NotificationServer.class.getName());

    private Map<InetAddress, MessageClient> clients = new HashMap<InetAddress, MessageClient>();
    private List<MessageClient> newClients = new ArrayList<MessageClient>();

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public NotificationServer() {
        LOGGER.info("Starting NotificationServer");
        try {
            serverSocket = new ServerSocket(54321);
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
        LOGGER.info("Stopping Socket Plugin ");

    }

    private void acceptIncomingCalls() {
        BufferedReader in = null;
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line = in.readLine();
            LOGGER.info("Incoming message " + line);

            NotificationMessage notificationMessage = NotificationMessageParser.getInstance().parseNotificationLine(line);

            LOGGER.info("Extracted message " + notificationMessage);

            handleAction(notificationMessage, clientSocket, line);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            Util.closeQuietly(in);
            Util.closeQuietly(clientSocket);
        }
    }

    private void handleAction(NotificationMessage notificationMessage, Socket clientSocket, String originalLine) {
        if (notificationMessage == NotificationMessage.REGISTER) {
            MessageClient newClient = new MessageClient(clientSocket.getInetAddress(), NotificationMessageParser.getInstance().extractPortNumber(originalLine));
            clients.put(clientSocket.getInetAddress(), newClient);
            newClients.add(newClient);
        } else if (notificationMessage == NotificationMessage.UNREGISTER) {
            clients.remove(clientSocket.getInetAddress());
        } else if (notificationMessage == NotificationMessage.HEART_BEAT) {
            clients.get(clientSocket.getInetAddress()).beat();
        }
    }

    public Map<InetAddress, MessageClient> getClients() {
        return clients;
    }

    public List<MessageClient> getNewClients() {
        return newClients;
    }

    // TODO make this private
    public class MessageClient {

        private final InetAddress inetAddress;
        private final int portToConnectTo;
        private long time;

        public MessageClient(InetAddress inetAddress, int portToConnectTo) {
            this.inetAddress = inetAddress;
            this.portToConnectTo = portToConnectTo;
            time = System.nanoTime();
        }

        public InetAddress getInetAddress() {
            return inetAddress;
        }

        public int getPortToConnectTo() {
            return portToConnectTo;
        }

        public void beat() {
            long newTime = System.nanoTime();
            LOGGER.info("Time diff " + (newTime - time));
            time = newTime;
        }
    }
}
