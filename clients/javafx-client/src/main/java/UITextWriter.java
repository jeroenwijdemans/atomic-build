import javafx.scene.control.TextArea;

/**
 * User: jeroenwijdemans
 * Date: 4/8/13
 */
public class UITextWriter {

    private final TextArea textArea;

    public UITextWriter(TextArea textArea) {
        this.textArea = textArea;
    }

    public void writeText(String text) {
        textArea.appendText(text + "\n");
    }
}
