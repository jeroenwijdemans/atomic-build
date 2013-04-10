package atomic.server.plugins;

import atomic.server.domain.AlarmPhase;

import java.util.logging.Logger;

public class LogPlugin implements Plugin {

    private static final Logger LOGGER = Logger.getLogger(LogPlugin.class.getName());

    private long counter = 0;

    @Override
    public void prepareActionOn() {
    }

    @Override
    public void takeActionOn(AlarmPhase alarmPhase) {

        if (counter++ % 450 == 0) {
            counter = 1;

            LOGGER.info(String.format("Current phase is %s", alarmPhase.getPhase()));
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutdown initiated");
    }
}
