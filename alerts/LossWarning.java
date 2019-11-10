/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.alerts;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import paint_overhaul.other.PaintCanvas;

/**
 * This class creates a warning if a loss is going to occur when saving.
 * This will only be called if a file, for example, is trying to be saved
 * as a png when it was originally a jpg.
 * @author Dylan
 */
public class LossWarning {
    private final PaintCanvas paintCanvas;
    /**
     * Constructor for the object
     * @param paintCanvas PaintCanvas object for the application
     */
    public LossWarning(PaintCanvas paintCanvas){
        this.paintCanvas = paintCanvas;

    }
    /**
     * Shows the loss warning to the user. 
     */
    public void setLossWarningAlert(){
        Alert alert = createLossWarningAlert();
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");
                
        alert.getButtonTypes().setAll(yesButton, cancelButton);
        
        Optional<ButtonType> result = alert.showAndWait();
        handleButtonTypeLogic(result, yesButton, cancelButton);
    }
    /**
     * Function to set the header for the smart save alert. 
     * @return Alert to show. 
     */
    private Alert createLossWarningAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText("WARNING! You have changed the file extension. The"
                + " file may become unusable or losses may occur. ");
        alert.setContentText("Do you wish to procced with file format " + 
                paintCanvas.getFileExtension(paintCanvas.getSavedFile()).toUpperCase()
                +"?");
        return alert;
    }
    /**
     * This function handles the logic for the buttons shown in the window. 
     * @param result Optional object
     * @param yesButton Yes button for the window.
     * @param noButton No button for the window. 
     * @param cancelButton Cancel button for the window. 
     */
    private void handleButtonTypeLogic(Optional<ButtonType> result, 
            ButtonType yesButton, ButtonType cancelButton ){
            if(result.get()==yesButton){
                WritableImage writableImage = paintCanvas.snapshotCurrentCanvas();
                //String ext = getFileExtension(file);
                if(paintCanvas.getFileExtension(paintCanvas.getOpenedFile()).equals("png")){
                    paintCanvas.savePNGImage(writableImage, paintCanvas.getSavedFile());
                }
                else{
                    paintCanvas.saveOtherImageTypes(writableImage, paintCanvas.getSavedFile());
                }
                paintCanvas.setHasBeenModified(false);
                }
            else if(result.get() == cancelButton){
                //paintCanvas.getSavedFile().delete();
            }
    }
}
