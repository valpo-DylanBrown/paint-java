/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.other;

import java.io.File;
import java.net.URL;
import paint_overhaul.threads.AutoSaveThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class for the CS-250 Microsoft Pain(t) rebuild. 
 * This function creates the scene and loads the controller. It also sets the 
 * icon of the application.
 * @author dylan
 * @version 4.0
 */
public class Main extends Application {
    public static final double MIN_PROGRAM_WIDTH = 400;
    public static final double MIN_PROGRAM_HEIGHT = 150;
    public static final String PROGRAM_NAME = "PAIN(t)";
    public static final double DEFAULT_WIDTH = 1024;
    public static final double DEFAULT_HEIGHT = 760;
    
    public static FXMLPaintController paintController;
    public PaintCanvas paintCanvas;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fmxl_paint.fxml"));
        Scene scene = new Scene(root, DEFAULT_WIDTH , DEFAULT_HEIGHT);
        stage.setTitle(PROGRAM_NAME);
        stage.setScene(scene);
        stage.setMinWidth(MIN_PROGRAM_WIDTH);
        stage.setMinHeight(MIN_PROGRAM_HEIGHT);
        /* If you need to disable the windows Min, Max, and Close
         * do this
         * stage.initStyle(StageStyle.UNDECORATED);
        */
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setMaximized(true);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
