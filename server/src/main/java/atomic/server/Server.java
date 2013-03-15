package atomic.server;

import atomic.server.plugins.LogPlugin;
import atomic.server.plugins.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private final List<Plugin> plugins;

    public Server() {
        List<Plugin> startPlugins = new ArrayList<Plugin>();
        startPlugins.add(new LogPlugin());

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

        }
    }

}
