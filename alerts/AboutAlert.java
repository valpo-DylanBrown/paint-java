/**
 * All alert objects for the application. 
 */
package paint_overhaul.alerts;

import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class to create an about alert for the application. This class sets the
 * icon of the window, and creates an including the information about the 
 * application.
 * @author Dylan
 * @since 3.0
 */
public class AboutAlert {
    /*
    * Relative path for file location
    */
    String iconFilePath = "src/paint_overhaul/icons/alertIcon.png";
    /**
     * Setup for the alert. This function also shows the alert to the user. 
     */
    public void createAboutAlert(){
        Alert alert = new Alert(AlertType.INFORMATION);
        ImageView icon = loadIcon();
        alert.setTitle("About");
        alert.setHeaderText("About PAIN(T) by Dylan Brown");
        alert.setContentText("A program that re-creates and improves "
                + "Microsoft Paint.\n\n \t\t\tLast Updated: 11/09/2019");
        alert.getDialogPane().setGraphic(icon);
        alert.showAndWait();
    }
    /**
     * Internal function to load the icon into the window. 
     * @return ImageView for the icon. 
     */
    private ImageView loadIcon(){
        File file = new File(iconFilePath);
        Image image = new Image(file.toURI().toString(), 50, 50, true, true);
        ImageView icon = new ImageView(image);
        return icon;
    }
}
