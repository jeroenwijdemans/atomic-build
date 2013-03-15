package atomic.server;

import atomic.server.plugins.LogPlugin;
import atomic.server.plugins.Plugin;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class ServerTest {

    private Server server;

    @BeforeMethod
    public void init() {
        server = new Server(new ArrayList<Plugin>());
    }

    @Test
    public void canAddPluginToServer() {
        assertThat(server.getPlugins().size(), is(equalTo(0)));

        Plugin sendUtpMessagePlugin = new LogPlugin();
        server.addPlugin(sendUtpMessagePlugin);

        assertThat(server.getPlugins().size(), is(equalTo(1)));
        assertThat(server.getPlugins().get(0), is(sameInstance((Plugin)sendUtpMessagePlugin)));
    }

}