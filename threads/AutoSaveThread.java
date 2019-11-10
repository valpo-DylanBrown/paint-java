/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import paint_overhaul.other.Main;

/**
 * Thread for auto-saving and logging of tools. 
 * @author dylan
 */
public class AutoSaveThread {
    private final Thread thread;
    int timeElapsed = 0;
    int saveInterval;
    private final File logFile;
    private final String logLocation = "src/paint_overhaul/logs/log.txt";
    String currentTool = "NONE";
    /**
     * Thread constructor. Sets the log file location, enforces log rule, 
     * creates a new thread and handles the auto-save always. Sets daemon to 
     * true.
     * @param saveInterval Desired interval to save (IN SECONDS)
     */
    public AutoSaveThread(int saveInterval){
        logFile = new File(logLocation);
        checkLogDeletion();
        this.saveInterval = saveInterval;
        thread = new Thread(() -> {
            while(true){
                handleAutoSave();
            }
        });
        thread.setDaemon(true);
        
    }
    /**
     * Safer way to start the thread.
     */
    public void startAutoSaveThread(){
        thread.start();
    }
    /**
     * Handles the auto-save every x seconds according the the save interval.
     * Keeps a count to send to the label, and sleeps the thread for one second.
     */
    private void handleAutoSave(){
        try{
            for(int i=saveInterval; i>0; i--){
                int currentCount=i;
                timeElapsed++;
                Platform.runLater(() -> {
                    Main.paintController.setAutoSaveLabel("Saving in: " +  currentCount);
                });
                Thread.sleep(1000);
            }
        }
        catch(Exception e){}
        try{
            Platform.runLater(() -> {
                try{
                    Main.paintController.getPaintCanvas().autoSaveCanvasToFile();
                }
                catch(Exception e){
                    System.out.println("No canvas");
                }
            });  
        }
        catch(IllegalArgumentException e){
            System.out.println("No canvas");
        } 
    }
    /**
     * Getter for the log file
     * @return Log File
     */
    public File getLogFile(){
        return logFile;
    }
    /**
     * Getter for the time elapsed
     * @return Time until next auto-save
     */
    public int getTimeElapsed(){
        return timeElapsed;
    }
    /**
     * Logs the current tool in use.
     * @param newTool Tool to log
     * @throws IOException if file can not be opened
     */
    public void logTool(String newTool) throws IOException{
        FileWriter fWriter;
        fWriter = new FileWriter(logFile,true);
        fWriter.append(currentTool + ": " + timeElapsed + " seconds" + System.getProperty("line.separator"));
        fWriter.close();
        currentTool = newTool;
        timeElapsed = 0;
    }
    /**
     * Logs file opening and saving
     * @param fileName Name of the file
     * @param mode true if opening, false if saving
     * @throws IOException if file can not be opened
     */
    public void logFile(String fileName, boolean mode) throws IOException{
        FileWriter fWriter;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        
        fWriter = new FileWriter(logFile,true);
        if(mode){
            fWriter.append(fileName + " Opened at: " + dateFormat.format(date) + System.getProperty( "line.separator" ));
            timeElapsed = 0;
        }
        else{
            fWriter.append(fileName + " Saved at: " + dateFormat.format(date)+ System.getProperty( "line.separator" ));
        }
        fWriter.close();
    }
    /**
     * Log Rule. If the log has been unmodified for three days, delete the log 
     * file.
     */
    private void checkLogDeletion(){
        long lastLogFileModification = new Date().getTime() - logFile.lastModified();
        int dayThreshold = 3*24*60*60*1000;
        
        if(lastLogFileModification > dayThreshold){
            logFile.delete();
        }
        
    }
}
