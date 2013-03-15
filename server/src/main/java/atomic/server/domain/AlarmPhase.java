package atomic.server.domain;

public class AlarmPhase {

    private Phase phase = Phase.GREEN;

    public Phase getPhase() {
        return phase;
    }

    public void increase() {
        if (phase == Phase.GREEN) {
            phase = Phase.YELLOW;
        }
        else if (phase == Phase.YELLOW) {
            phase = Phase.RED;
        }
    }

    public void standDown() {
        phase = Phase.GREEN;
    }
}