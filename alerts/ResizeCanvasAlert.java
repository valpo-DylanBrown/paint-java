/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.alerts;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import paint_overhaul.other.PaintCanvas;

/**
 * Class to create an alert when resizing the canvas. 
 * The function handles different actions from the user-input. If the user
 * wishes to resize the canvas, the width and height are changed. If not, the 
 * dialog exits. 
 * @author Dylan
 * @since 3.0
 */
public class ResizeCanvasAlert {
    private PaintCanvas paintCanvas;
    /**
     * Constructor
     * @param pc PaintCanvas object
     */
    public ResizeCanvasAlert(PaintCanvas pc){
        this.paintCanvas=pc;
    }
    /**
     * This function creates the resize dialog. 
     */
    public void createResizeDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Resize Canvas");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        TextField width = new TextField();
        width.setPromptText("Width:");
        TextField height = new TextField();
        height.setPromptText("Height");
        createGridPane(gridPane, width, height);
        dialog.getDialogPane().setContent(gridPane);
        // Request focus on the username field by default.
        Platform.runLater(() -> width.requestFocus());
        
        dialog.showAndWait();
        
        
        WritableImage beforeResizeImage = paintCanvas.snapshotCurrentCanvas();
        PixelReader pixelReader = beforeResizeImage.getPixelReader();
        if(pixelReader == null){
            return;
        }
        
        if(width.getText().isEmpty() || height.getText().isEmpty()){
            return;
        }
        
        int beforeResizeWidth = (int)paintCanvas.getCanvas().getWidth();
        int beforeResizeHeight = (int)paintCanvas.getCanvas().getHeight();
        int afterResizeWidth = Integer.parseInt(width.getText());
        int afterResizeHeight = Integer.parseInt(height.getText());
        
        afterResizeWidth = Math.abs(afterResizeWidth);
        afterResizeHeight = Math.abs(afterResizeHeight);
        
        WritableImage afterResizeImage = new WritableImage(afterResizeWidth,afterResizeHeight);
        PixelWriter pixelWriter = afterResizeImage.getPixelWriter();
        
        for(int resizeY = 0; resizeY < afterResizeHeight; resizeY++) {
            int previousY = (int)Math.round((double)resizeY / afterResizeHeight * beforeResizeHeight);
            for(int resizeX = 0; resizeX < afterResizeWidth; resizeX++) {
                int previousX = (int)Math.round((double)resizeX / afterResizeWidth * beforeResizeWidth);
                pixelWriter.setArgb(resizeX, resizeY, pixelReader.getArgb(previousX, previousY));
            }
        }
        paintCanvas.getCanvas().setWidth(afterResizeImage.getWidth());
        paintCanvas.getCanvas().setHeight(afterResizeImage.getHeight());
        paintCanvas.getGraphicsContext().drawImage(afterResizeImage,0,0);
        paintCanvas.setHasBeenModified(true);
    }
    /**
     * Internal function to create a grid pane for the dialog elements.
     * @param gridPane Grid Pane that will be created.
     * @param width  TextField to hold width data.
     * @param height TextField to hold height data
     */
    private void createGridPane(GridPane gridPane, TextField width, TextField height){
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        gridPane.add(new Label("Width:"), 0, 0);
        gridPane.add(width, 1, 0);
        gridPane.add(new Label("Height"), 2, 0);
        gridPane.add(height, 3, 0);
        gridPane.add(new Label("Resize the canvas to your desired pixels "
                + "WARNING: This can not be undone."),4,1);

    }
}
