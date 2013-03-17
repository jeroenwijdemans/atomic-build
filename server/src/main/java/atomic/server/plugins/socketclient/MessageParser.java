package atomic.server.plugins.socketclient;

import java.util.logging.Logger;

public class MessageParser {

    private static final Logger LOGGER = Logger.getLogger(MessageParser.class.getName());

    private static MessageParser socketMessageParser = new MessageParser();

    private MessageParser() {
    }

    public static MessageParser getInstance() {
        return socketMessageParser;
    }

    public SocketMessage parseMessageLine(String line) {
        if (line == null) {
            LOGGER.info("Empty or multi line message not supported");
            return null;
        }

        line = line.toUpperCase();
        if (line.equals("RAISE")) {
            return SocketMessage.RAISE;
        } else if (line.equals("STAND_DOWN") || line.equals("STANDDOWN") || line.equals("STAND DOWN")) {
            return SocketMessage.STAND_DOWN;
        } else if (line.equals("ESCALATE")) {
            return SocketMessage.ESCALATE;
        } else {
            LOGGER.warning("Ignoring unknown message arrived : " + line);
        }
        return null;
    }

}
