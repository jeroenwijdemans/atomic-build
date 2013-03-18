package atomic.server.domain;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public class AlarmPhaseTest {

    private AlarmPhase alarmPhase;

    @BeforeMethod
    public void init() {
        alarmPhase = new AlarmPhase();
    }

    @Test
         public void shouldBeGreenOnInitialisation() {
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
    }

    @Test
    public void shouldBeYellowAfterChangeFromGreen() {
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
        alarmPhase.increase();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.YELLOW)));
    }

    @Test
    public void shouldBeGreenAfterStandDown() {
        alarmPhase.increase();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.YELLOW)));
        alarmPhase.standDown();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
    }

    @Test
    public void shouldBeOrangeAfterTwoInitialIncreases() {
        alarmPhase.increase();
        alarmPhase.increase();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.ORANGE)));
    }

    @Test
    public void shouldBeRedAfterEscalation() {
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
        alarmPhase.escalate();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.RED)));
    }

    @Test
    public void shouldRemainAfterDecrease() {
        alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_GREEN();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
        alarmPhase.decrease();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
    }

    @Test
    public void shouldBeGreenAfterDecreaseOnYellow() {
        alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_YELLOW();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.YELLOW)));
        alarmPhase.decrease();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.GREEN)));
    }

    @Test
    public void shouldBeYellowAfterDecreaseOnOrange() {
        alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_ORANGE();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.ORANGE)));
        alarmPhase.decrease();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.YELLOW)));
    }

    @Test
    public void shouldBeOrangeAfterDecreaseOnRed() {
        alarmPhase = AlarmPhaseObjectMother.ALARM_PHASE_RED();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.RED)));
        alarmPhase.decrease();
        assertThat(alarmPhase.getPhase(), is(equalTo(Phase.ORANGE)));
    }

    @Test(enabled = false)
    public void shouldSendNotificationWhenPhaseIsChanged() {
        fail("Need to implement");
    }
}
