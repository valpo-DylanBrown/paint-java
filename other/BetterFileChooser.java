/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.other;

import javafx.stage.FileChooser;

/**
 * Class for a more efficient file chooser
 * @author Dylan
 */
public class BetterFileChooser {
    FileChooser fileChooser = new FileChooser();
    String fcTitle;
    /**
     * Constructor for BetterFileChooser. 
     * @param title Desired title of File Chooser. 
     */
    public BetterFileChooser(String title){
        this.fcTitle = title;
        configureFileChooser(fileChooser, fcTitle);
    }
    /**
     * Configures the File Chooser.
     * This function configures the file chooser. The function passes the string
     * to the title of the window and adds extension filters to filter images in.
     * Called from {@link #openNewFile()}, {@link #handleSaveAs()}, and
     * {@link #handleSave()} 
     * @param fileChooser  file chooser to edit
     * @param title string to set title of the window
     */
    private void configureFileChooser(FileChooser fileChooser, String title){
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.jpeg", "*.jpg",
                "*.png", "*.tiff", "*.tif","*.bmp", "*.JPEG", "*.JPG","*.PNG",
                "*.TIFF","*.TIF", "*.BMP"),
            new FileChooser.ExtensionFilter("JPEG", "*.jpeg", "*.jpg",
                "*.JPEG", "*.JPG"),
            new FileChooser.ExtensionFilter("PNG", "*.png", "*.PNG"),
            new FileChooser.ExtensionFilter("BMP", "*.bmp", "*.BMP"),
            new FileChooser.ExtensionFilter("TIFF", "*.tiff", "*.tif",
                "*.TIFF", "*.tif")
            );
    }
    /**
     * Getter for the FileChooser. 
     * @return fileChooser. 
     */
    public FileChooser getFileChooser(){
        return fileChooser;
    }
}
