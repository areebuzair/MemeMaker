package MemeMaker;

// import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
// import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Meme Latest.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setCanvas();
        controller.initialize();

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {

                    case DELETE:
                    case BACK_SPACE:
                        controller.deleteSelectedNode();
                        break;
                    case HOME:
                        controller.bringCanvasHome();
                        break;
                    default:
                    // System.out.println(event.getCode());
                        break;
                }
            }
        });

        primaryStage.setTitle("Meme Maker");
        primaryStage.setScene(new Scene(root));
        // Load the CSS file
        String cssFile = getClass().getResource("application.css").toExternalForm();

        // Apply the stylesheet to the scene
        primaryStage.getScene().getStylesheets().add(cssFile);

        Image icon = new Image("MemeMaker/icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw"); // Forces software rendering
        // Alternatively, use "es2" or "es2, d3d" to prefer OpenGL
        System.setProperty("prism.order", "es2, d3d");
        launch(args);
    }
}