package atomic.server;

import atomic.server.plugins.LogPlugin;
import atomic.server.plugins.Plugin;
import org.testng.annotations.AfterMethod;
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

    @AfterMethod
    public void after() {
        server.shutdown();
    }

    @Test
    public void canAddPluginToServer() {
        assertThat(server.getPlugins().size(), is(equalTo(0)));

        Plugin logPlugin = new LogPlugin();
        server.addPlugin(logPlugin);

        assertThat(server.getPlugins().size(), is(equalTo(1)));
        assertThat(server.getPlugins().get(0), is(sameInstance((Plugin) logPlugin)));
    }

}
