package atomic.server.plugins.notifcationsocketserver;

import atomic.server.CustomerThreadFactory;
import atomic.server.Util;
import atomic.server.domain.AlarmPhase;
import atomic.server.domain.Phase;
import atomic.server.plugins.Plugin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class NotificationPlugin implements Plugin {

    private static final Logger LOGGER = Logger.getLogger(NotificationPlugin.class.getName());

    private final NotificationServer messageServer;

    private Phase previousPhase;

    private Executor executor = Executors.newFixedThreadPool(1, new CustomerThreadFactory("noti-plugin"));

    public NotificationPlugin(NotificationServer messageServer) {
        this.messageServer = messageServer;
        executor.execute(messageServer);
    }

    @Override
    public void prepareActionOn() {

    }

    @Override
    public void takeActionOn(AlarmPhase alarmPhase) {

        // TODO wtf. make this good
        if (messageServer.getNewClients().size() > 0) {
            LOGGER.info("Updating new client list");
            NotificationServer.MessageClient mc = messageServer.getNewClients().get(0);
            sendUpdateMessageTo(
                    alarmPhase.getPhase(),
                    mc.getInetAddress(), mc.getPortToConnectTo()
            );
            LOGGER.info("Removing item from new client list");
            messageServer.getNewClients().remove(mc);


        }


        if (alarmPhase.getPhase() != previousPhase) {
            previousPhase = alarmPhase.getPhase();
            LOGGER.info(String.format("Phase change detected: sending update to %d clients.", messageServer.getClients().size()));
            for (NotificationServer.MessageClient messageClient : messageServer.getClients().values()) {
                sendUpdateMessageTo(
                        alarmPhase.getPhase(),
                        messageClient.getInetAddress(), messageClient.getPortToConnectTo()
                );
            }
        }
    }

    private void sendUpdateMessageTo(Phase phase, InetAddress host, int port) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, port);
            clientSocket.getOutputStream().write(phase.name().getBytes());
        } catch (IOException e) {
            LOGGER.severe("Cannot send msg : " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            Util.closeQuietly(clientSocket);
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down NotificationPlugin");
    }
}
