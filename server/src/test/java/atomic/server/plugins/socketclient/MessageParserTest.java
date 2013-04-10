package atomic.server.plugins.socketclient;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageParserTest {

    private MessageParser messageParser = MessageParser.getInstance();

    @Test
    public void shouldReturnEmptyMessageWhenMessageCannotBeParsed() {
        assertThat(messageParser.parseMessageLine(null), is(is(equalTo(SocketMessage.UNKNOWN))));
        assertThat(messageParser.parseMessageLine(""), is(is(equalTo(SocketMessage.UNKNOWN))));
        assertThat(messageParser.parseMessageLine("aapnootmies"), is(equalTo(SocketMessage.UNKNOWN)));
    }

    @Test
    public void shouldParseEscalateWhenCorrectLineArg() {
        assertThat(messageParser.parseMessageLine("escalate"), is(equalTo(SocketMessage.ESCALATE)));
        assertThat(messageParser.parseMessageLine("ESCAlate"), is(equalTo(SocketMessage.ESCALATE)));
    }

    @Test
    public void shouldParseRaiseWhenCorrectLineArg() {
        assertThat(messageParser.parseMessageLine("raise"), is(equalTo(SocketMessage.RAISE)));
    }

    @Test
    public void shouldParseStandDownWhenCorrectLineArg() {
        assertThat(messageParser.parseMessageLine("stand_down"), is(equalTo(SocketMessage.STAND_DOWN)));
        assertThat(messageParser.parseMessageLine("stand down"), is(equalTo(SocketMessage.STAND_DOWN)));
        assertThat(messageParser.parseMessageLine("STANDDOWN"), is(equalTo(SocketMessage.STAND_DOWN)));
    }
}