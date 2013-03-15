package atomic.server;

import org.testng.annotations.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RunnerTest {

    @Test
    public void afterStartupApplicationMustRespondUntilStopped() {
        Runner runner = new Runner();
        assertThat(runner.isRunning(), is(equalTo(true)));
        runner.stopRunning();
        assertThat(runner.isRunning(), is(equalTo(false)));
    }

}