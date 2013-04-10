import atomic.server.plugins.socketclient.Destructible;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// http://youtubeinmp4.com/
public class ClientFxListenerApplication extends Application implements Destructible {

    private static final Logger LOGGER = Logger.getLogger(ClientFxListenerApplication.class.getName());

    private Button selfDestructBtn = new Button();
    private Button exitBtn = new Button();
    private Label hostAndPortLabel = new Label();
    private TextArea infoText = new TextArea();

    private Ellipse green;
    private Ellipse yellow;
    private Ellipse orange;
    private Ellipse red;

    private ClientListener clientListener;
    private static final int MAX_WAIT = 3;

    final ScheduledExecutorService destructionScheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        createTrayIcon();

        primaryStage.setTitle("Nuclear Thread Listener ...");

        hostAndPortLabel.setLayoutX(10);
        hostAndPortLabel.setLayoutY(10);
        hostAndPortLabel.setText(String.format("%s - %s", ClientListener.HOST, ClientListener.PORT));

        infoText.setLayoutX(10);
        infoText.setLayoutY(40);

        selfDestructBtn.setLayoutX(310);
        selfDestructBtn.setLayoutY(210);
        selfDestructBtn.setText("Self Destruct");

        exitBtn.setLayoutX(422);
        exitBtn.setLayoutY(210);
        exitBtn.setText("Exit");

        infoText.setEditable(false);

        selfDestructBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startDestructionSequence();
            }
        });

        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clientListener.stop();
                System.exit(-1);
            }
        });

        Group root = new Group();

        root.getChildren().add(selfDestructBtn);
        root.getChildren().add(exitBtn);
        root.getChildren().add(hostAndPortLabel);
        root.getChildren().add(infoText);

        green = drawLightBulb(360, 20, Color.GRAY);
        yellow = drawLightBulb(390, 20, Color.GRAY);
        orange = drawLightBulb(420, 20, Color.GRAY);
        red = drawLightBulb(450, 20, Color.GRAY);

        root.getChildren().add(green);
        root.getChildren().add(yellow);
        root.getChildren().add(orange);
        root.getChildren().add(red);

        Scene scene = new Scene(root, 480, 260);
        primaryStage.setScene(scene);
        primaryStage.show();

        clientListener = new ClientListener(
                new UITextWriter(infoText),
                new UILedLight(green, yellow, orange, red, this)
        );
    }

    @Override
    public void startDestructionSequence() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                selfDestructBtn.disableProperty().set(true);
                destructionScheduler.scheduleAtFixedRate(
                        new ThreadAgnosticJavaFxExplosionRunner(MAX_WAIT, destructionScheduler, infoText)
                        , 0, 1, TimeUnit.SECONDS);
            }
        });
    }

    private Ellipse drawLightBulb(int x, int y, Color color) {
        return EllipseBuilder.create()
                .centerX(x)
                .centerY(y)
                .radiusX(10)
                .radiusY(10)
                .strokeWidth(3)
                .stroke(Color.GRAY)
                .fill(color)
                .build();
    }

    /**
     * Code copied from some website... but not working on a Mac.
     */
    private void createTrayIcon() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("nuclear.png");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("N-B");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(listener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
                LOGGER.severe("Error adding icon to tray");
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions
            LOGGER.info("No system tray available");
        }
        // ...
        // some time later
        // the application state has changed - update the image
        if (trayIcon != null) {
            // trayIcon.setImage(updatedImage);
        }
    }



}
