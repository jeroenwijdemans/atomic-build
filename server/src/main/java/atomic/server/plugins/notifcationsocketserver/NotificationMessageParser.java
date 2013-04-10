package atomic.server.plugins.notifcationsocketserver;

import java.util.logging.Logger;

public class NotificationMessageParser {

    private static final Logger LOGGER = Logger.getLogger(NotificationMessageParser.class.getName());

    private static NotificationMessageParser notificationMessageParser = new NotificationMessageParser();

    private NotificationMessageParser() {
    }

    public static NotificationMessageParser getInstance() {
        return notificationMessageParser;
    }

    public NotificationMessage parseNotificationLine(String line) {
        if (line == null) {
            LOGGER.info("Empty or multi line message not supported");
            return NotificationMessage.UNKNOWN;
        }

        line = line.toUpperCase();
        if (line.startsWith("REGISTER")) {
            return NotificationMessage.REGISTER;
        } else if (line.startsWith("REVOKE") || line.startsWith("UNREGISTER")) {
            return NotificationMessage.UNREGISTER;
        } else if (line.startsWith("HEART_BEAT")) {
            return NotificationMessage.HEART_BEAT;
        } else {
            LOGGER.warning("Ignoring unknown message arrived : " + line);
        }
        return NotificationMessage.UNKNOWN;
    }

    public int extractPortNumber(String line) {
        return Integer.valueOf(line.split(" ")[1]);
    }
}
