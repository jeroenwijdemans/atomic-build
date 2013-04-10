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
            phase = Phase.ORANGE;
        }
        else if (phase == Phase.ORANGE) {
            phase = Phase.RED;
        }
    }

    public void decrease() {
        if (phase == Phase.RED) {
            phase = Phase.ORANGE;
        }
        else if (phase == Phase.ORANGE) {
            phase = Phase.YELLOW;
        }
        else if (phase == Phase.YELLOW) {
            phase = Phase.GREEN;
        }
    }

    public void standDown() {
        phase = Phase.GREEN;
    }

    public void escalate() {
        phase = Phase.RED;
    }

    @Override
    public String toString() {
        return "AlarmPhase[" + phase + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlarmPhase)) {
            return false;
        }
        AlarmPhase that = (AlarmPhase) o;
        if (phase != that.phase)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return phase != null ? phase.hashCode() : 0;
    }
}
