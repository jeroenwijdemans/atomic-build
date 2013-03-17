package atomic.server.plugins.socketclient;

import atomic.server.domain.AlarmPhase;
import atomic.server.plugins.Plugin;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketPlugin implements Plugin {

    private static final Logger LOGGER = Logger.getLogger(SocketPlugin.class.getName());

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
        if (socketServer.getLastMessage() != null) {
            LOGGER.info("Going to: " + socketServer.getLastMessage());
        }

    }

    @Override
    public void takeActionOn(AlarmPhase alarmPhase) {
        SocketMessage lastMessage = socketServer.takeMessage();
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
    }

    @Override
    public void shutdown() {
        socketServer.stopRunning();
    }


}
