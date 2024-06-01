package MemeMaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ComboBox<String> fontComboBox;

    @FXML
    private ColorPicker fontColorPicker;

    private Node selectedNode;

    public void initialize() {
        // Populate the ComboBox with system font families
        fontComboBox.getItems().addAll(Font.getFamilies());

        // Set a default font
        fontComboBox.getSelectionModel().select("System");

        // Set a default color for the ColorPicker
        fontColorPicker.setValue(Color.BLACK);
    }

    public void press(MouseEvent mouseEvent) {
        selectedNode = (Node) mouseEvent.getSource();

        // Bring the selected node to the front of the next node
        Pane parent = (Pane) selectedNode.getParent();
        int index = parent.getChildren().indexOf(selectedNode);

        if (index < parent.getChildren().size() - 1) {
            parent.getChildren().remove(selectedNode);
            parent.getChildren().add(index + 1, selectedNode);
        }
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
            ((Label) selectedNode).setFont(Font.font(selectedFont, 20));
        }
    }
}
