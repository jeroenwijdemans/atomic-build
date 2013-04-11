import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class ThreadAgnosticJavaFxExplosionRunner implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ThreadAgnosticJavaFxExplosionRunner.class.getName());

    private final ScheduledExecutorService destructionScheduler;
    private final TextArea infoText;

    private int maxWaitTime;
    private MediaPlayer mediaPlayer;
    private Stage stage;

    public ThreadAgnosticJavaFxExplosionRunner(int maxWaitTime, ScheduledExecutorService destructionScheduler, TextArea infoText) {
        this.maxWaitTime = maxWaitTime;
        this.destructionScheduler = destructionScheduler;
        this.infoText = infoText;
        prepareExplosion();
    }

    private void prepareExplosion() {
        File nuclearMovie = null;
        try {
            URL url = this.getClass().getResource("Nuclear_Explosion.mp4");
            nuclearMovie = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot find: " + nuclearMovie.getAbsolutePath());
        }
        if (!nuclearMovie.exists()) {
            throw new RuntimeException("Cannot find: " + nuclearMovie.getAbsolutePath());
        }

        mediaPlayer = new MediaPlayer(new Media(nuclearMovie.toURI().toString()));

        MediaView mv = createFullScreenStretchedMediaView();

        StackPane root = new StackPane();
        root.getChildren().add(mv);
        stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
    }

    private MediaView createFullScreenStretchedMediaView() {
        MediaView mv = new MediaView(mediaPlayer);
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        mv.setPreserveRatio(false);
        return mv;
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                infoText.appendText(maxWaitTime + "\n");

                if (maxWaitTime <= 0) {
                    LOGGER.severe("Exploding sequence set");
                    explode();
                    destructionScheduler.shutdown();
                }
                maxWaitTime--;
            }

            private void explode() {
                stage.show();
                stage.setFullScreen(true);
                mediaPlayer.play();
            }

        });

    }
}
