package atomic.server;

import atomic.server.domain.AlarmPhase;
import atomic.server.plugins.LogPlugin;
import atomic.server.plugins.Plugin;
import atomic.server.plugins.socketclient.SocketCommandServer;
import atomic.server.plugins.socketclient.SocketPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final List<Plugin> plugins;
    private AlarmPhase alarmPhase = new AlarmPhase();

    public Server() {
        List<Plugin> startPlugins = new ArrayList<Plugin>();
        startPlugins.add(new LogPlugin());
        startPlugins.add(new SocketPlugin(new SocketCommandServer()));

        plugins = startPlugins;
    }

    public Server(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public List<Plugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public void run() {
        for (Plugin plugin : plugins) {
            plugin.prepareActionOn();
        }
        for (Plugin plugin : plugins) {
            plugin.takeActionOn(alarmPhase);
        }
    }

    public void shutdown() {
        for (Plugin plugin : plugins) {
            plugin.shutdown();
        }

    }

    public void initialise() {
        LOGGER.info("Initialising server ...");
    }
}
