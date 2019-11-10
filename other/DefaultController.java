/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.other;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import paint_overhaul.threads.AutoSaveThread;

/**
 * Default controller for the program. 
 * @author Dylan
 * @since 4.0
 */
public class DefaultController implements Initializable {
    protected PaintCanvas paintCanvas;
    protected AutoSaveThread autoSaveThread;
    
    /**
     * Initialization function. Sets the PaintCanvas for the program. 
     * @param url not used.
     * @param resources not used.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources){
        paintCanvas = Main.paintController.getPaintCanvas();
        autoSaveThread = Main.paintController.getAutoSaveThread();
        
    }
}
