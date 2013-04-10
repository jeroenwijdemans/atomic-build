package atomic.server.plugins.socketclient;

import atomic.server.CustomerThreadFactory;
import atomic.server.domain.AlarmPhase;
import atomic.server.plugins.Plugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketPlugin implements Plugin {

    private static final Logger LOGGER = Logger.getLogger(SocketPlugin.class.getName());

    private SocketCommandServer socketServer;
    private Executor executor = Executors.newFixedThreadPool(1, new CustomerThreadFactory("sock-plugin"));

    public SocketPlugin(SocketCommandServer socketServer) {
        this.socketServer = socketServer;
        executor.execute(socketServer);
    }

    @Override
    public void prepareActionOn() {
        if (socketServer.getLastMessage() == SocketMessage.UNKNOWN ||
                socketServer.getLastMessage() == SocketMessage.NO_MESSAGE) {
        }
        else {
            LOGGER.info("Going to: " + socketServer.getLastMessage());
        }
    }

    @Override
    public void takeActionOn(AlarmPhase alarmPhase) {
        SocketMessage lastMessage = socketServer.takeMessage();

        switch (lastMessage) {
            case NO_MESSAGE:
                break;
            case RAISE:
                alarmPhase.increase();
                break;
            case ESCALATE:
                alarmPhase.escalate();
                break;
            case STAND_DOWN:
                alarmPhase.standDown();
                break;
            case UNKNOWN:
            default:
                LOGGER.severe(String.format("Unknown Message %s... cannot take action!", lastMessage));
        }
    }

    @Override
    public void shutdown() {
        socketServer.stopRunning();

    }

}
