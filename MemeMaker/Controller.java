package MemeMaker;

import javafx.scene.input.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    void visitReadMe(ActionEvent event) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(
                java.net.URI.create("https://github.com/areebuzair/MemeMaker/tree/main?tab=readme-ov-file#mememaker"));
    }

    @FXML
    private ScrollPane assetbrowser;

    @FXML
    private Button defaultfolder;

    @FXML
    private AnchorPane CanvasArea;

    @FXML
    private TextField canvasHeight;

    @FXML
    private TextField canvasWidth;

    @FXML
    private Button folderchooser;

    @FXML
    private ComboBox<String> fontComboBox;

    @FXML
    private ColorPicker fontColorPicker;

    @FXML
    private VBox TextSettings;

    private AnchorPane drawingCanvas;

    private Scale scale;

    private double moveDragX;
    private double moveDragY;

    private Node selectedNode;

    @FXML
    private ColorPicker myColorPicker;

    @FXML
    private TextArea textInputArea;

    private void openTextSettings() {
        textInputArea.setText(((Label) selectedNode).getText());
        // Set a default font
        fontComboBox.getSelectionModel().select(((Label) selectedNode).getFont().getName());

        // Set a default color for the ColorPicker
        fontColorPicker.setValue((Color) ((Label) selectedNode).getTextFill());

        TextSettings.setVisible(true);
    }
    
    private void selectNode(Node sourceNode) {
        if (selectedNode != null)
            selectedNode.getStyleClass().remove("selected");
        selectedNode = sourceNode;
        selectedNode.getStyleClass().remove("selected");
        selectedNode.getStyleClass().add("selected");
        if (selectedNode instanceof Label) {
            openTextSettings();
        } else {
            TextSettings.setVisible(false);
        }
    }

    @FXML
    public void initialize() {
        // Populate the ComboBox with system font families
        fontComboBox.getItems().addAll(Font.getFamilies());

        // Set a default font
        fontComboBox.getSelectionModel().select("System");

        // Set a default color for the ColorPicker
        fontColorPicker.setValue(Color.BLACK);

        TextSettings.setVisible(false);
    }

    public void changeColor(ActionEvent evemt) {
        Color myColor = myColorPicker.getValue();
        drawingCanvas.setBackground(new Background(new BackgroundFill(myColor, null,
                null)));
    }

    @FXML
    void changeFontColor(ActionEvent event) {
        if (selectedNode instanceof Label) {
            // Get the selected color from the ColorPicker
            Color selectedColor = fontColorPicker.getValue();

            // Set the default color to black if no color is selected
            if (selectedColor == null) {
                selectedColor = Color.BLACK;
            }

            // Apply the selected color to the font of the selected Label
            ((Label) selectedNode).setTextFill(selectedColor);
        }
    }

    @FXML
    void changeFont(ActionEvent event) {
        // Get the selected font family from the ComboBox
        String selectedFont = fontComboBox.getSelectionModel().getSelectedItem();
        if (selectedFont != null && selectedNode instanceof Label) {
            // Apply the selected font family to the selected Label
            ((Label) selectedNode).setFont(Font.font(selectedFont, ((Label) selectedNode).getFont().getSize()));
        }
    }

    public void setAssetbrowser(String folderpath) {
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
                copyImageView.setFitWidth(clickedImage.getWidth());
                copyImageView.setFitHeight(clickedImage.getHeight());

                copyImageView.setOnMousePressed(e -> Pressed(e));
                copyImageView.setOnMouseDragged(e -> Drag(e));
                copyImageView.getStyleClass().add("selectable");

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
        // label.setMaxWidth(300);
        // label.setId("resultLabel");
        label.setWrapText(true); // Enable word wrapping for the label
        label.setPrefHeight(Label.USE_COMPUTED_SIZE);
        label.getStyleClass().add("selectable");

        label.setOnMousePressed(e -> Pressed(e));
        label.setOnMouseDragged(e -> Drag(e));

        drawingCanvas.getChildren().add(label);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        
        selectNode(label);

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
        setAssetbrowser("./src/MemeMaker/Meme");
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
        drawingCanvas.setPrefSize(
                Double.valueOf(canvasWidth.getText()),
                Double.valueOf(canvasHeight.getText())); // Set initial size
        drawingCanvas.setMinWidth(AnchorPane.USE_PREF_SIZE);
        drawingCanvas.setMaxWidth(AnchorPane.USE_PREF_SIZE);
        drawingCanvas.setMinHeight(AnchorPane.USE_PREF_SIZE);
        drawingCanvas.setMaxHeight(AnchorPane.USE_PREF_SIZE);
        drawingCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        AnchorPane.setTopAnchor(drawingCanvas, (CanvasArea.getPrefHeight() - drawingCanvas.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(drawingCanvas, (CanvasArea.getPrefWidth() - drawingCanvas.getPrefWidth()) / 2);

        // Create a CanvasArea and set the AnchorPane as its content
        CanvasArea.getChildren().add(drawingCanvas);

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
            double scaleFactor = 0.0078125; // Adjust this factor as needed
            double newScale = Math.max(scale.getX() + deltaY * scaleFactor, 0.03125); // Ensure scale doesn't become
                                                                                      // negative

            scale.setX(newScale);
            scale.setY(newScale);

        });
    }

    @FXML
    void saveImage(ActionEvent event) {
        scale.setX(1);
        scale.setY(1);

        if (selectedNode != null) {
            selectedNode.getStyleClass().remove("selected");
            selectedNode = null;
            TextSettings.setVisible(false);
        }

        // Create a WritableImage object with the same dimensions as the AnchorPane
        WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(),
                (int) drawingCanvas.getHeight());

        // Render the AnchorPane to the WritableImage
        drawingCanvas.snapshot(null, writableImage);
        // LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = currentDateTime.format(formatter);

        FileChooser fileChooser = new FileChooser();
        // Set extension filter for PNG files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("Meme_" + formattedDateTime + ".png");

        // Show save file dialog
        File file = fileChooser.showSaveDialog((Stage) drawingCanvas.getScene().getWindow());

        try {
            // Write the image to the file
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            System.out.println("Screenshot saved to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Error saving screenshot: ");
        }
    }

    @FXML
    void CanvasPan(MouseEvent event) {
        Node sourceNode;
        drawingCanvas.requestFocus();
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            sourceNode = drawingCanvas;
        } else {
            return;
        }
        double newX = event.getSceneX() - moveDragX;
        double newY = event.getSceneY() - moveDragY;

        if (event.isShiftDown()) {
            double newW = drawingCanvas.getPrefWidth() + newX / scale.getX();
            double newH = drawingCanvas.getPrefHeight() + newY / scale.getY();
            if (newW > 0) {
                drawingCanvas.setPrefWidth((double) Math.round(newW));
                canvasWidth.setText(String.valueOf(Math.round(newW)));
            }
            if (newH > 0) {
                drawingCanvas.setPrefHeight((double) Math.round(newH));
                canvasHeight.setText(String.valueOf(Math.round(newH)));
            }
        } else {
            sourceNode.setTranslateX(sourceNode.getTranslateX() + newX);
            sourceNode.setTranslateY(sourceNode.getTranslateY() + newY);
        }

        moveDragX = event.getSceneX();
        moveDragY = event.getSceneY();
        event.consume();
        // // System.out.println(Dragger.getLayoutX());
        // System.out.println(":---");
    }

    @FXML
    void CanvasPanStart(MouseEvent event) {
        // Node sourceNode;
        if (event.getButton() != javafx.scene.input.MouseButton.MIDDLE) {
            if (selectedNode != null) {
                selectedNode.getStyleClass().remove("selected");
                selectedNode = null;
                TextSettings.setVisible(false);

            }
            return;
        }

        moveDragX = (event.getSceneX());
        moveDragY = (event.getSceneY());

        event.consume();
    }

    // @FXML
    void Drag(MouseEvent event) {
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            return;
        }
        Node sourceNode = (Node) event.getSource();
        double newX = event.getSceneX() - moveDragX;
        double newY = event.getSceneY() - moveDragY;

        if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
            if (selectedNode instanceof Label) {
                ((Region) selectedNode).setPrefWidth(((Region) sourceNode).getPrefWidth() + newX / scale.getX());
                if (((Label) selectedNode).getFont().getSize() + newY / (scale.getY() * 5) < 500)
                    ((Label) selectedNode).setFont(new javafx.scene.text.Font(
                            ((Label) selectedNode).getFont().getName(),
                            ((Label) selectedNode).getFont().getSize() + newY / (scale.getY() * 5)));
                // System.out.println(((Label) selectedNode).getFont().getSize());
            } else {
                ((ImageView) selectedNode).setFitHeight(((ImageView) sourceNode).getFitHeight() + newY / scale.getY());
                ((ImageView) selectedNode).setPreserveRatio(event.isShiftDown());
                ((ImageView) selectedNode).setFitWidth(((ImageView) sourceNode).getFitWidth() + newX / scale.getX());
            }

        } else {
            if (event.isShiftDown()) {
                if (Math.abs(newX) > Math.abs(newY)) {
                    newY = 0;
                } else {
                    newX = 0;
                }
            }
            sourceNode.setTranslateX(sourceNode.getTranslateX() + (newX) / scale.getX());
            sourceNode.setTranslateY(sourceNode.getTranslateY() + (newY) / scale.getY());
        }

        moveDragX = event.getSceneX();
        moveDragY = event.getSceneY();

        event.consume();
    }

    // @FXML
    void Pressed(MouseEvent event) {
        drawingCanvas.requestFocus();
        if (event.getButton() == javafx.scene.input.MouseButton.MIDDLE) {
            return;
        }
        Node sourceNode = (Node) event.getSource();

        selectNode(sourceNode);

        // Prepare for drag
        moveDragX = (event.getSceneX());
        moveDragY = (event.getSceneY());

        if (event.isAltDown()) {
            Pane parent = (Pane) selectedNode.getParent();
            int index = parent.getChildren().indexOf(selectedNode);

            if (index > 0) {
                parent.getChildren().remove(selectedNode);
                parent.getChildren().add(index - 1, selectedNode);
            }
        }
        // System.out.println(event.getSceneX());
        event.consume();
    }

    public void deleteSelectedNode() {
        if (selectedNode == null)
            return;
        drawingCanvas.getChildren().remove(selectedNode);
        selectedNode = null;
        TextSettings.setVisible(false);
    }

    @FXML
    void ChangeLabelText(KeyEvent event) {
        String newText = textInputArea.getText();

        // Check if the selectedNode is a Label before casting
        if (selectedNode instanceof Label) {
            Label label = (Label) selectedNode;
            label.setText(newText);
        } else {
            System.out.println("Selected node is not a Label.");
        }
    }

    public void bringCanvasHome() {
        drawingCanvas.setTranslateX(0);
        drawingCanvas.setTranslateY(0);
        AnchorPane.setTopAnchor(drawingCanvas, 0.0);
        AnchorPane.setLeftAnchor(drawingCanvas, 0.0);
        scale.setX(1);
        scale.setY(1);
    }

    @FXML
    void changeCanvasHeight(ActionEvent event) {
        String text = ((TextField) event.getSource()).getText();

        // Check if the text is a number using a regular expression
        if (text.matches("-?\\d+(\\.\\d+)?") && Double.valueOf(text) > 0) {
            drawingCanvas.setPrefHeight(Double.valueOf(text));
        } else {
            ((TextField) event.getSource()).setText(String.valueOf(Math.round(drawingCanvas.getPrefHeight())));
        }
    }

    @FXML
    void changeCanvasWidth(ActionEvent event) {
        String text = ((TextField) event.getSource()).getText();

        // Check if the text is a number using a regular expression
        if (text.matches("-?\\d+(\\.\\d+)?") && Double.valueOf(text) > 0) {
            drawingCanvas.setPrefWidth(Double.valueOf(text));
        } else {
            ((TextField) event.getSource()).setText(String.valueOf(Math.round(drawingCanvas.getPrefWidth())));
        }
    }

}
