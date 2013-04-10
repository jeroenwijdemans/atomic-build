package atomic.server;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Runner implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    private final Server server;
    private volatile boolean running = true;

    public static void main(String args[]) {
        new Runner();
    }

    public Runner() {
        this(new Server());
    }

    public Runner(Server server) {
        this.server = server;
        LOGGER.info("Starting server...");
        Executors.newFixedThreadPool(1)
                .execute(this);
        LOGGER.info("Started server...");
    }

    public boolean isRunning() {
        return running;
    }

    public void stopRunning() {
        running = false;
        server.shutdown();
        LOGGER.info("Stopping server...");
    }

    @Override
    public void run() {
        server.initialise();
        while (isRunning()) {
            server.run();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Stopped server");
    }
}
