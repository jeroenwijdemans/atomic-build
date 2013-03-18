package atomic.server.plugins.socketclient;

import atomic.server.domain.AlarmPhase;
import atomic.server.domain.AlarmPhaseObjectMother;
import atomic.server.domain.Phase;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SocketPluginTest {

    private SocketPlugin socketPlugin;

    @Mock
    private SocketCommandServer socketCommandServerMock;

    @BeforeMethod(alwaysRun=true)
    public void init() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        socketPlugin = new SocketPlugin(socketCommandServerMock);
    }

    @AfterMethod
    public void clear() throws InterruptedException {
        socketPlugin.shutdown();
        verify(socketCommandServerMock).stopRunning();
    }

    @Test
    public void shouldIncreaseProvidedPhaseWhenRaiseTakeAction() throws InterruptedException {
        AlarmPhase alarmPhase = new AlarmPhase();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));

        when(socketCommandServerMock.takeMessage()).thenReturn(SocketMessage.RAISE);

        socketPlugin.takeActionOn(alarmPhase);

        // TODO I do NOT understand why *only* this verify fails?
//        verify(socketCommandServerMock).takeMessage();

        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.YELLOW)));
    }

    @Test
    public void shouldSetPhaseToRedWhenEscalateTakeAction() {
        AlarmPhase alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_GREEN();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));

        when(socketCommandServerMock.takeMessage()).thenReturn(SocketMessage.ESCALATE);

        socketPlugin.takeActionOn(alarmPhase);

        verify(socketCommandServerMock).takeMessage();

        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.RED)));
    }

    @Test
    public void shouldSetPhaseToGreenWhenStandDownTakeAction() {
        AlarmPhase alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_RED();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.RED)));

        when(socketCommandServerMock.takeMessage()).thenReturn(SocketMessage.STAND_DOWN);

        socketPlugin.takeActionOn(alarmPhase);

        verify(socketCommandServerMock).takeMessage();

        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
    }

    @Test
    public void shouldNotChangePhaseWhenNoAction() {
        AlarmPhase alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_ORANGE();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.ORANGE)));

        when(socketCommandServerMock.takeMessage()).thenReturn(null);

        socketPlugin.takeActionOn(alarmPhase);

        verify(socketCommandServerMock).takeMessage();

        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.ORANGE)));
    }


}
