/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.alerts;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class creates the help menu from Help-&gt;Help Contents. 
 * The file path is held in a relative location from the alert. It also
 * loads the icon into the window. 
 * @author Dylan
 * @since 3.0
 */
public class HelpAlert {
    public String content;
    //public TextArea area;
    String filePath  = "src/paint_overhaul/other/help.txt";
    String iconFilePath = "src/paint_overhaul/icons/alertIcon.png";
    /**
     * Setup for the help alert. Loads file from relative location, sets the 
     * icon, title, and header text. 
     */
    public void createAlert(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help contents for Pain(t)");

        content = this.setContent(filePath);
        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefWidth(550);
        area.setPrefHeight(750);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        ImageView icon = loadIcon();
        alert.getDialogPane().setGraphic(icon);
        alert.showAndWait();
    }
    /**
     * Internal function to set the content of the window.
     * @param filePath  Location of the file to be loaded. 
     * @return String of content. 
     */
    private String setContent(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            System.out.println("error");
        }
        return contentBuilder.toString();
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
