package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import paint_overhaul.other.Main;
import paint_overhaul.other.PaintCanvas;

/**
 * SelectionRectangle object. This handles the select, move, and copy. 
 * See {@link PaintCanvas#canvasSetup()}
 * @author dylan
 */
public class SelectionRectangle {
    private WritableImage image;
    
    double origX, origY;
    double endX, endY;
    boolean copyMode = false;
    boolean pasteMode = false;
    private ImageView currentSelection;
    /**
     * 4 Input constructor
     * @param startX Starting x
     * @param startY Starting y
     * @param endX Ending x
     * @param endY Ending y
     */
    public SelectionRectangle(double startX, double startY, double endX, double endY) {
        origX = startX;
        origY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    /**
     * 2-Input constructor
     * @param x x value
     * @param y y value
     */
    public SelectionRectangle(double x, double y) {
        origX = x;
        origY = y;
        endX = x;
        endY = y;
    }
    /**
     * Sets the end value
     * @param x ending x value
     * @param y ending y value
     */
    public void setEnd(double x, double y){
        endX = x;
        endY = y;
    }
    /**
     * Sets the image of the image view to the selection received and handles
     * its movement.
     * @param gc GraphicsContext of the Canvas
     */
    public void setImage( GraphicsContext gc){
        selectionSetup(gc);
        Paint colorBeforeErase = gc.getFill();
        currentSelection.setX(origX);
        currentSelection.setY(origY);
        currentSelection.setOnMousePressed(event -> {
            //Main.paintController.getStaticPane().getChildren().remove(Main.paintController.getPaintCanvas().getRectangle().getRect());
            if(!copyMode){
                gc.setFill(Color.WHITE);
                gc.fillRect(origX, origY, Math.abs(endX - origX), Math.abs(endY - origY));
                Main.paintController.getPaintCanvas().setRedrawnImage(Main.paintController.getPaintCanvas().getCanvas().snapshot(null, null));
            }
            
            origX = (event.getX()- currentSelection.getX());
            origY = (event.getY()- currentSelection.getY());
        });
        currentSelection.setOnMouseDragged(event -> {
            currentSelection.setX(event.getX()- origX);
            currentSelection.setY(event.getY()- origY);
            event.consume();
        });
        currentSelection.setOnMouseReleased(event -> {
            Main.paintController.getStaticPane().getChildren().remove(currentSelection);
            gc.drawImage(currentSelection.getImage(),currentSelection.getX(),currentSelection.getY());
            currentSelection = null;
            Main.paintController.getPaintCanvas().setCurrentSelection(null);
            setCopyMode(false);
            gc.setFill(colorBeforeErase);
        });
        Main.paintController.getStaticPane().getChildren().add(currentSelection);
        gc.setFill(colorBeforeErase); 
    }
    /**
     * Internal function to help get the image selected.
     * @param gc GraphicsContext for the canvas.
     */
    private void selectionSetup( GraphicsContext gc){
        WritableImage oldImg = new WritableImage(
            (int)Main.paintController.getPaintCanvas().getCanvas().getWidth(), 
            (int)Main.paintController.getPaintCanvas().getCanvas().getHeight());
        
        Main.paintController.getPaintCanvas().getCanvas().snapshot(null, oldImg);
        if(origX > endX){
            double temp = endX;
            endX = origX;
            origX = temp; 
        }
        if(origY > endY){
            double temp = endY;
            endY = origY;
            origY = temp;
        }
        WritableImage newImage = new WritableImage(oldImg.getPixelReader(), 
                (int)origX, (int)origY, (int)Math.abs(origX-endX), 
                (int)Math.abs(origY-endY));  
        
        currentSelection = new ImageView(newImage);
    }
    /**
     * Getter for the original x value
     * @return Original x
     */
    public double getOrigX(){
        return origX;
    }
    /**
     * Getter for the ending x value
     * @return ending x
     */
    public double getEndX(){
        return endX;
    }
    /**
     * Getter for the original y value
     * @return Original y
     */
    public double getOrigY(){
        return origY;
    }
    /**
     * Getter for the ending y value
     * @return ending y
     */
    public double getEndY(){
        return endY;
    }
    /**
     * Getter for the current ImageView
     * @return ImageView of selection
     */
    public ImageView getCurrentSelection(){
        return currentSelection;
    }
    /**
     * Setter for the original x value
     * @param origX original x value
     */
    public void setOrigX(double origX){
        this.origX = origX;
    }
    /**
     * Setter for the ending x value
     * @param endX ending x value
     */
    public void setEndX(double endX){
        this.endX = endX;
    }
    /**
     * Setter for the original y value
     * @param origY original y value
     */
    public void setOrigY(double origY){
        this.origY = origY;
    }
    /**
     * Setter for the ending y value
     * @param endY ending y value
     */
    public void setEndY(double endY){
        this.endY = endY;
    }
    /**
     * Sets the mode of the selection according to the boolean input.
     * @param bool true if mode is copy, false otherwise
     */
    public void setCopyMode(boolean bool){
        this.copyMode = bool;
    }  
}
