package atomic.server.plugins;

import atomic.server.domain.AlarmPhase;

public interface Plugin {

    /**
     * Called first and can be used to communicate and upcoming changes
     *
     */
    void prepareActionOn();

    /**
     * Allows the plugin to take action
     *
     * By passing in the alarmPhase itself we hard couple plugins to the logic of the system. This will probably be
     * changed pretty soon.
     *
     * @param alarmPhase current phase
     */
    void takeActionOn(final AlarmPhase alarmPhase);

    /**
     * Called by the application to notify the plugin that it is going to shutdown.
     */
    void shutdown();

}
