/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.alerts;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import paint_overhaul.other.BetterFileChooser;
import paint_overhaul.other.Main;
import paint_overhaul.other.PaintCanvas;

/**
 * Creates the pop-up window for the smart save notification.
 * @author Dylan
 */
public class SmartSaveAlert {
    private PaintCanvas paintCanvas;
    private BetterFileChooser betterFC;
    File file;
    /**
     * Constructor for the smart save alert. 
     * @param paintCanvas PaintCanvas object for the application. 
     * @param betterFC BetterFileChooser for the application. 
     */
    public SmartSaveAlert(PaintCanvas paintCanvas, BetterFileChooser betterFC){
        this.paintCanvas = paintCanvas;
        this.betterFC = betterFC;
    }
    /**
     * Creates the alert for the smart save notification. 
     * @see #createCloseAlertDialog() 
     * @see #handleButtonTypeLogic(Optional, ButtonType, ButtonType, ButtonType) 
     */
    public void setSmartSaveAlert(){
        Alert alert = createCloseAlertDialog();
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel");
                
        alert.getButtonTypes().setAll(yesButton,noButton,cancelButton);
        
        Optional<ButtonType> result = alert.showAndWait();
        handleButtonTypeLogic(result, yesButton, noButton, cancelButton);
    }
    /**
     * Function to set the header for the smart save alert. 
     * @return Alert to show. 
     */
    private Alert createCloseAlertDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText("Your file is not currently saved. Would you "
                + "like to save it?");
        alert.setContentText("Please choose an option");
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
            ButtonType yesButton, ButtonType noButton, ButtonType cancelButton ){
            if(result.get()==yesButton){
                if(paintCanvas.getSavedFile() == null){
                    paintCanvas.setSavedFile(betterFC.getFileChooser().showSaveDialog(Main.paintController.getBorderPane().getScene().getWindow())); 
                    }
                paintCanvas.saveCanvasToFile(paintCanvas.getSavedFile());
                //Main.paintController.getAutoSaveThread().shutdownThread();
                Platform.exit();
            }
            
            else if(result.get()==noButton){
                
                Platform.exit();
            }
            else if(result.get() == cancelButton){
            }
    }
}
