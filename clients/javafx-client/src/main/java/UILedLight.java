import atomic.server.plugins.socketclient.Destructible;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.logging.Logger;

/**
 * User: jeroenwijdemans
 * Date: 4/8/13
 */
public class UILedLight {

    private static final Logger LOGGER = Logger.getLogger(UILedLight.class.getName());

    private final Ellipse green;
    private final Ellipse yellow;
    private final Ellipse orange;
    private final Ellipse red;
    private final Destructible destructible;

    public UILedLight(Ellipse green, Ellipse yellow, Ellipse orange, Ellipse red, Destructible destructible) {
        this.green = green;
        this.yellow = yellow;
        this.orange = orange;
        this.red = red;
        this.destructible = destructible;
    }

    public void enableLight(String value) {
        LOGGER.info("Changing to " + value);
        if ("GREEN".equals(value)) {
            green.setFill(Color.GREEN);
            yellow.setFill(Color.GRAY);
            orange.setFill(Color.GRAY);
            red.setFill(Color.GRAY);
        }
        else if ("YELLOW".equals(value)) {
            green.setFill(Color.GRAY);
            yellow.setFill(Color.YELLOW);
            orange.setFill(Color.GRAY);
            red.setFill(Color.GRAY);
        }
        else if ("ORANGE".equals(value)) {
            green.setFill(Color.GRAY);
            yellow.setFill(Color.GRAY);
            orange.setFill(Color.ORANGE);
            red.setFill(Color.GRAY);
        }
        else if ("RED".equals(value)) {
            green.setFill(Color.GRAY);
            yellow.setFill(Color.GRAY);
            orange.setFill(Color.GRAY);
            red.setFill(Color.RED);
            destructible.startDestructionSequence();
        }

    }
}
