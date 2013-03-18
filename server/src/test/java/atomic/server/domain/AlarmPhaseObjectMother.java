package atomic.server.domain;

public class AlarmPhaseObjectMother {

    public static AlarmPhase ALARM_PHASE_GREEN() {
        return new AlarmPhase();
    }

    public static AlarmPhase ALARM_PHASE_YELLOW() {
        AlarmPhase alarmPhase = new AlarmPhase();
        alarmPhase.increase();
        return alarmPhase;
    }

    public static AlarmPhase ALARM_PHASE_ORANGE() {
        AlarmPhase alarmPhase = new AlarmPhase();
        alarmPhase.escalate();
        alarmPhase.decrease();
        return alarmPhase;
    }

    public static AlarmPhase ALARM_PHASE_RED() {
        AlarmPhase alarmPhase = new AlarmPhase();
        alarmPhase.escalate();
        return alarmPhase;
    }
}
