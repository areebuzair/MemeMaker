package MemeMaker;

// import javafx.application.Application;
// import javafx.beans.value.ChangeListener;
// import javafx.beans.value.ObservableValue;
// import javafx.scene.Scene;
// import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;

import java.time.LocalDateTime;
// import javafx.event.Event;
// import javafx.event.EventHandler;
// import java.time.LocalDate;
import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
// import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
// import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
// import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
// import java.io.File;
import java.io.IOException;

import javafx.scene.control.TextArea;
// import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private ScrollPane assetbrowser;

    @FXML
    private Button defaultfolder;

    @FXML
    private AnchorPane CanvasArea;

    @FXML
    private Button folderchooser;

    private AnchorPane drawingCanvas;

    private Scale scale;

    private double moveDragX;
    private double moveDragY;

    private Node selectedNode;

    @FXML
    private ColorPicker myColorPicker;

    @FXML
    private TextArea textInputArea;

    public void changeColor(ActionEvent evemt) {
        Color myColor = myColorPicker.getValue();
        drawingCanvas.setBackground(new Background(new BackgroundFill(myColor, null,
                null)));
    }

    public void setAssetbrowser(String folderpath) {
        // String folderPath = "D:/Areeb"; // Replace with your folder path
        List<Image> images = loadImagesFromFolder(folderpath);

        GridPane gridPane = createGridPane(images);

        assetbrowser.setContent(gridPane);
        assetbrowser.setFitToWidth(true);
        assetbrowser.setFitToHeight(true);
    }

    private GridPane createGridPane(List<Image> images) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Set column constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50); // 50% width for each column
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50); // 50% width for each column
        gridPane.getColumnConstraints().addAll(column1, column2);

        // Set row constraints
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(70); // Equal height for each row
        for (int i = 0; i < images.size(); i++) {
            gridPane.getRowConstraints().add(rowConstraints);
        }

        int col = 0;
        int row = 0;
        for (Image image : images) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(70); // Set the width of each image
            imageView.setPreserveRatio(true);

            // Add event handler to each image
            imageView.setOnMouseClicked(event -> {
                Image clickedImage = ((ImageView) event.getSource()).getImage();
                ImageView copyImageView = new ImageView(clickedImage);

                copyImageView.setOnMousePressed(e -> Pressed(e));
                copyImageView.setOnMouseDragged(e -> Drag(e));

                drawingCanvas.getChildren().add(copyImageView);
                AnchorPane.setTopAnchor(copyImageView, 0.0);
                AnchorPane.setLeftAnchor(copyImageView, 0.0);

            });

            gridPane.add(imageView, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        return gridPane;
    }

    @FXML
    void AddTextBox(ActionEvent event) {
        Label label = new Label("Text will appear here");
        label.setMaxWidth(300);
        // label.setId("resultLabel");
        label.setWrapText(true); // Enable word wrapping for the label
        label.setPrefHeight(Label.USE_COMPUTED_SIZE);

        label.setOnMousePressed(e -> Pressed(e));
        label.setOnMouseDragged(e -> Drag(e));

        drawingCanvas.getChildren().add(label);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);

    }

    private List<Image> loadImagesFromFolder(String folderPath) {
        List<Image> images = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path or folder does not exist.");
            return images;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Folder is empty.");
            return images;
        }

        for (File file : files) {
            if (file.isFile() && isImageFile(file.getName())) {
                images.add(new Image(file.toURI().toString()));
            }
        }

        return images;
    }

    private boolean isImageFile(String fileName) {
        String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tif", ".tiff" };
        for (String extension : imageExtensions) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void SetDefaultBrowser(ActionEvent event) {
        setAssetbrowser("./Meme");
    }

    @FXML
    void ChooseImageFolder() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");

        Stage stage = (Stage) assetbrowser.getScene().getWindow();

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            // System.out.println( "No path selected");
            setAssetbrowser(selectedDirectory.getAbsolutePath());
        } else {
            System.out.println("No path selected");
        }
    }

    @FXML
    public void setCanvas() {
        selectedNode = null;
        drawingCanvas = new AnchorPane();
        drawingCanvas.setPrefSize(400, 50); // Set initial size
        drawingCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        AnchorPane.setTopAnchor(drawingCanvas, 0.0);
        AnchorPane.setLeftAnchor(drawingCanvas, 0.0);
        // canvasField.setBackground(new Background(new BackgroundFill(Color.YELLOW,
        // CornerRadii.EMPTY, Insets.EMPTY)));

        // Add some text inside the AnchorPane
        // Text text = new Text("Scroll to zoom");
        // drawingCanvas.getChildren().add(text);
        // AnchorPane.setTopAnchor(text, 10.0);
        // AnchorPane.setLeftAnchor(text, 10.0);

        // Create a CanvasArea and set the AnchorPane as its content
        CanvasArea.getChildren().add(drawingCanvas);

        // CanvasArea.setBackground(new Background(new BackgroundFill(Color.RED,
        // CornerRadii.EMPTY, Insets.EMPTY)));
        // Disable the default scroll wheel function
        CanvasArea.setOnScrollStarted(e -> e.consume());
        // CanvasArea.setOnScroll(e -> e.consume());
        CanvasArea.setOnScrollFinished(e -> e.consume());
        // Disable the default scroll wheel function
        // CanvasArea.addEventFilter(ScrollEvent.ANY, ScrollEvent::consume);

        // Create a Scale transform
        scale = new Scale(1, 1);

        // Add the Scale transform to the AnchorPane
        drawingCanvas.getTransforms().add(scale);

        // Set up scroll event handler on AnchorPane
        CanvasArea.setOnScroll((ScrollEvent event) -> {
            event.consume();
            double deltaY = event.getDeltaY();
            double scaleFactor = 0.01; // Adjust this factor as needed
            double newScale = Math.max(scale.getX() + deltaY * scaleFactor, 0.1); // Ensure scale doesn't become
                                                                                  // negative

            scale.setX(newScale);
            scale.setY(newScale);

            // drawingCanvas.setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5;");

            // text.setText(String.valueOf(deltaY));

        });
    }

    @FXML
    void saveImage(ActionEvent event) {
        scale.setX(1);
        scale.setY(1);

        // Create a WritableImage object with the same dimensions as the AnchorPane
        WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(),
                (int) drawingCanvas.getHeight());

        // Render the AnchorPane to the WritableImage
        drawingCanvas.snapshot(null, writableImage);
        // LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Printing the current date and time
        // System.out.println("Current Date and Time: " );

        // Create a file to save the screenshot
        File file = new File("Meme - " + String.valueOf(currentDateTime).replace(".", "").replace(":", "") + " .png");

        try {
            // Write the image to the file
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            System.out.println("Screenshot saved to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Error saving screenshot: " + ex.getMessage());
        }
    }

    @FXML
    void CanvasPan(MouseEvent event) {
        Node sourceNode;
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            sourceNode = drawingCanvas;
        } else {
            return;
        }
        double newX = event.getSceneX() - moveDragX;
        double newY = event.getSceneY() - moveDragY;

        sourceNode.setTranslateX(newX);
        sourceNode.setTranslateY(newY);
        event.consume();
        // // System.out.println(Dragger.getLayoutX());
        // System.out.println(":---");
    }

    @FXML
    void CanvasPanStart(MouseEvent event) {
        Node sourceNode;
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            sourceNode = drawingCanvas;
        } else {
            if(selectedNode != null){
                selectedNode.getStyleClass().remove("selected");
                selectedNode = null;
            }
            return;
        }

        // Prepare for drag
        moveDragX = (event.getSceneX() - sourceNode.getTranslateX());
        moveDragY = (event.getSceneY() - sourceNode.getTranslateY());

        event.consume();
        // System.out.println(event.getSceneX());
    }

    // @FXML
    void Drag(MouseEvent event) {
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            return;
        }
        Node sourceNode = (Node) event.getSource();
        double newX = event.getSceneX() - moveDragX;
        double newY = event.getSceneY() - moveDragY;

        sourceNode.setTranslateX( sourceNode.getTranslateX() + ( newX ) / scale.getX());
        sourceNode.setTranslateY( sourceNode.getTranslateY() + ( newY ) / scale.getY());

        moveDragX = event.getSceneX();
        moveDragY = event.getSceneY();

        event.consume();
        System.out.println(sourceNode.getTranslateX());
        System.out.println(":---");
    }

    // @FXML
    void Pressed(MouseEvent event) {
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            return;
        }
        if (selectedNode != null)
            selectedNode.getStyleClass().remove("selected");
        Node sourceNode = (Node) event.getSource();
        selectedNode = sourceNode;
        selectedNode.getStyleClass().add("selected");

        // Prepare for drag
        moveDragX = (event.getSceneX());
        moveDragY = (event.getSceneY());

        if (selectedNode instanceof Label) {
            textInputArea.setText(((Label) event.getSource()).getText());
        }
        // System.out.println(event.getSceneX());
        event.consume();
    }

    public void deleteSelectedNode() {
        if (selectedNode == null)
            return;
        drawingCanvas.getChildren().remove(selectedNode);
        selectedNode = null;
    }

    @FXML
    void ChangeLabelText(KeyEvent event) {
        String newText = textInputArea.getText();
        // System.out.println("Fnc called");

        // Check if the selectedNode is a Label before casting
        if (selectedNode instanceof Label) {
            Label label = (Label) selectedNode;
            label.setText(newText);
        } else {
            System.out.println("Selected node is not a Label.");
        }
    }

}
