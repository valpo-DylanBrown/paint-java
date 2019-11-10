package paint_overhaul.other;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javax.imageio.ImageIO;
import paint_overhaul.constant.DrawingMode;
import paint_overhaul.constant.DrawingTools;
import paint_overhaul.shapes.*;

/**
 * This class is a better version of JavaFX's canvas. 
 * This class allows for easy drawing and zooming. 
 * @author dylan
 * @since 3.0
 * 
 */
public class PaintCanvas {
    private Canvas canvas;
    private GraphicsContext gc;
    private Color strokeColor;
    private Color fillColor;
    private Image redrawnImage;
    private File openedFile;
    private File savedFile;
    private DrawingTools drawTools;
    private DrawingMode drawMode;
    private PaintShape currentShape;
    private BetterRectangle rectangle;
    private ImageView currentSelection;
    private WritableImage selectedImage;
    private boolean hasBeenModified = false;
    private boolean isPolygon = false;
    private Stack<Image> undoHistory;
    private Stack<Image> redoHistory;
    private SelectionRectangle selection;
    private double currentZoom = 1;
    private Scale zoomScale;
    private boolean isZoomedOut = false;
    private int numSides = 5;
    private int fontSize;
    private Font font;
    private String userText;
    private String autoSaveLocation;
    
    /**
     * Constructor for PaintCanvas.
     * This function creates a new PaintCanvas object. It sets the undo/redo stacks,
     * the canvas, stroke/fill colors, graphics context, and the redrawn image.
     * It also sets up the canvas and draws it. 
     * @param canvas Canvas you wish to edit
     */
    public PaintCanvas(Canvas canvas){
        undoHistory = new Stack<>();
        redoHistory = new Stack<>();
        this.canvas = canvas;
        strokeColor = Color.BLACK;
        fillColor = Color.BLACK;
        drawTools = DrawingTools.NONE;
        autoSaveLocation = "src/paint_overhaul/autosave/autosave.";
        this.gc = canvas.getGraphicsContext2D();
        canvasSetup();
        redrawnImage = canvas.snapshot(null,null);
        redrawCanvas();
        
    }
    /**
     * Function to implement drawing on the canvas.
     * This function uses event handlers to listen for different mouse
     * actions on the canvas. It uses the DrawingTools enumeration to increase 
     * readability. The function keeps track of a general shape that is drawn
     * onto the canvas and popped into the undo stack. 
     *
     */
    private void canvasSetup(){
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
            if(drawTools == DrawingTools.NONE){
                return;
            }
            redrawnImage = canvas.snapshot(null,null);
            if(drawMode!=DrawingMode.SELECT){
               switch(drawTools){
                case PENCIL:
                    currentShape = new Pencil(e.getX(), e.getY());
                    break;
                case FILL:
                    currentShape = new Fill(e.getX(), e.getY(), getCanvas());
                    break;
                case LINE:
                    currentShape = new Line(e.getX(), e.getY());
                    break;
                case ERASER:
                    currentShape = new Eraser(e.getX(), e.getY());
                    break;
                case RECTANGLE:
                    currentShape = new BetterRectangle(e.getX(), e.getY());
                    break;
                case SQUARE:
                    currentShape = new Square(e.getX(), e.getY());
                    break;
                case CIRCLE:
                    currentShape = new Circle(e.getX(), e.getY());
                    break;
                case ELLIPSE:
                    currentShape = new Ellipse(e.getX(), e.getY());
                    break;
                case TRIANGLE:
                    currentShape = new Triangle(e.getX(), e.getY());
                    break;
                case POLYGON:
                    currentShape = new Polygon(e.getX(), e.getY());
                    break;
                case STAR:
                    currentShape = new Star(e.getX(), e.getY());
                    break;
                case TEXT:
                    currentShape = new Text(e.getX(), e.getY(), userText);
                    break;
                case EYEDROPPER:
                    Color color = redrawnImage.getPixelReader().getColor((int)e.getX(), (int)e.getY());
                    Main.paintController.getFillColorPicker().setValue(color);
                    setFillColor(color);
                    break;
                } 
               
            }
            else{
                if(currentSelection == null){
                    selection = new SelectionRectangle(e.getX(), e.getY());
                    rectangle = new BetterRectangle(e.getX(), e.getY());    
                }
            }
            hasBeenModified = true;
            });
         canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
                if(drawTools == DrawingTools.NONE || drawTools == DrawingTools.EYEDROPPER){
                    return;
                }
                redrawCanvas();
                
                if(drawMode!=DrawingMode.SELECT){
                    currentShape.setEnd(e.getX(), e.getY());
                    if(!currentShape.getIsPolygon()){
                      currentShape.draw(gc);
                    }
                    else{
                        currentShape.draw(gc,numSides);
                    }  
                }
                else{
                    if(currentSelection==null){
                        rectangle.setEnd(e.getX(), e.getY());
                        rectangle.drawSelection(gc);
                        selection.setEnd(e.getX(), e.getY());
                    }
                    
                    
                }
                hasBeenModified = true;
            });
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e)->{
                if(drawTools == DrawingTools.NONE || drawTools == DrawingTools.EYEDROPPER){
                    return;
                }
                undoHistory.add(redrawnImage);
                redrawCanvas();
                
                
                if(drawMode!=DrawingMode.SELECT){
                    currentShape.setEnd(e.getX(), e.getY());
                    if(!currentShape.getIsPolygon()){
                      currentShape.draw(gc);
                      }
                      else{
                          currentShape.draw(gc,numSides);
                      }  
                }
                else{
                    if(currentSelection == null){
                        rectangle.setEnd(e.getX(), e.getY());
                        rectangle.drawSelection(gc);
                        selection.setEnd(e.getX(), e.getY());
                        selection.setImage(gc);
                        currentSelection = selection.getCurrentSelection();
                        
                        //selection.setImage(gc);
                    }
                    
                    
                }
                
                redrawnImage = canvas.snapshot(null,null);
                hasBeenModified = true;
            });
    }
    /**
     * Un-does the last action.
     * This function controls the undo stack. It pushes the redo stack with the 
     * undo. Then pops the last image. Then it redraws the canvas.
     */
    public void undoLast(){
        if(undoHistory.isEmpty()){
            return;
        }
        redoHistory.push(redrawnImage);
        redrawnImage = undoHistory.pop();
        //Main.paintController.getStaticPane().getChildren().remove(selection.getCurrentSelection());
        redrawCanvas();
    }
    /**
     * Re-does the last undo.
     * This function controls the redo stack. It pushes the undo stack with redo.
     * It then pops the last image, and redraws the canvas.
     */
    public void redoLast(){
        if(redoHistory.isEmpty()){
            return;
        }
        undoHistory.push(redrawnImage);
        redrawnImage = redoHistory.pop();
        redrawCanvas();
    }
    /**
     * Redraws the canvas based off redrawnImage.
     */
    public void redrawCanvas(){
        gc.drawImage(redrawnImage, 0, 0, redrawnImage.getWidth(), redrawnImage.getHeight());
    }
    /**
     * Function to open an image from file.
     * This function takes a file and loads an image from it. Then, it sets
     * the openedFile to the file loaded and loads the image
     * @param imageFile Desired file to open
     * @see #loadImage(Image)
     */
    public void loadImageFromFille(File imageFile){
        Image image = null;
        try{
            image = new Image(new FileInputStream(imageFile.getAbsolutePath()));
        }
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
        openedFile = imageFile;
        //System.out.println(getFileExtension(openedFile));
        loadImage(image);
    }
    /**
     * Function to draw image to canvas.
     * This function sets the width and height of the canvas to that of the 
     * image. It then draws the image to the canvas.
     * @param image Desired image to draw
     */
    public void loadImage(Image image){
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        gc.drawImage(image,0,0,image.getWidth(),image.getHeight());
    }
    /**
     * Function to save canvas to file.
     * This function sets the saved file parameter. It then takes a snapshot of
     * the current canvas and writes it out to a file.
     * @param file Desired save location
     */
    public void saveCanvasToFile(File file) {
        setSavedFile(file);
        if(getFileExtension(file).equalsIgnoreCase(getFileExtension(openedFile))){
            WritableImage writableImage = snapshotCurrentCanvas();
            if(getFileExtension(openedFile).equals("png")){
                savePNGImage(writableImage, file);
            }
            else{
                saveOtherImageTypes(writableImage, file);
            }
            hasBeenModified = false;
        }
        else{
            Main.paintController.getLossWarning().setLossWarningAlert();
        }    
    }
    /**
     * Auto-saves the canvas using the original file extension to preserve loss.
     */
    public void autoSaveCanvasToFile() {
        String fullPath = autoSaveLocation + getFileExtension(openedFile);
        File autoSavedFile = new File(fullPath);
        WritableImage writableImage = snapshotCurrentCanvas();
        if(getFileExtension(autoSavedFile).equals("png")){
            savePNGImage(writableImage, autoSavedFile);
        }
        else{
            saveOtherImageTypes(writableImage, autoSavedFile);
        }
    }
    /**
     * Saving capability for PNG images. 
     * @param writableImage Image to be saved.
     * @param file File to be saved to.
     */
    public void savePNGImage(WritableImage writableImage, File file){
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try{
                ImageIO.write(renderedImage, getFileExtension(file), file);
            }
            catch(IOException e){
                System.out.println("File not found");
            }
    }
    /**
     * Saving capability for other file types. This rips out the transparent 
     * layer that is typically in the canvas. 
     * @param writableImage Image to be saved.
     * @param file File to be saved to.
     */
    public void saveOtherImageTypes(WritableImage writableImage, File file){
        BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null); 
        BufferedImage imageRGB = new BufferedImage(
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.OPAQUE);
        Graphics2D graphics = imageRGB.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        try{
            ImageIO.write(imageRGB, getFileExtension(file), file);
        }
        catch(IOException e){
            System.out.println("File not found");
        }
        graphics.dispose();
    }
    /**
     * Function that snapshots the current canvas.
     * This function creates a new writable image the size of the canvas.
     * It sets the parameters to transparent and snapshots the image.
     * @return Snapshot of the canvas
     */
    public WritableImage snapshotCurrentCanvas(){
        WritableImage writableImage = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, writableImage);
        return writableImage;
    }
    /**
     * Function that snapshots the current canvas with custom parameters, 
     * width, and height.This function creates a new writable image the 
     * size of the canvas.It sets the parameters to transparent and snapshots 
     * the image.
     * @param params Desired snapshot parameters
     * @param width Desired width
     * @param height Desired height
     * @return Snapshot of the canvas
     */
    public WritableImage snapshotCurrentCanvas(SnapshotParameters params, double width, double height){
        WritableImage writableImage = new WritableImage((int) width, (int) height);
        canvas.snapshot(params, writableImage);
        return writableImage;
    }
    
    /**
     * Function to zoom the canvas in.
     * This function gets the parent node of the canvas, removes the current 
     * transform, increases the current zoom by 5%, and applies the zoom to the
     * pane.
     * @see #applyZoom(Parent, double) 
     */
    public void zoomIn(){
        Parent parent = canvas.getParent();
        parent.getTransforms().remove(zoomScale);
        currentZoom += 0.05;
        applyZoom(parent, currentZoom);
        //zoomLabel.setText(Math.round(currentZoom*100) + "%");
    }
    /**
     * Function to set the zoom of the canvas.
     * This function creates a new scale based off the current zoom.
     * It then adds the transformation to the parent pane.
     * @param parent Parent of the canvas
     * @param zoom Desired zoom
     * @see #zoomIn()
     * @see #zoomOut() 
     * @see #zoomToX(double) 
     */
    private void applyZoom(Parent parent, double zoom){
        zoomScale = new Scale(zoom, zoom,0,0);
        parent.getTransforms().add(zoomScale);
    }
    /**
     * Function to zoom the canvas out.
     * This function gets the parent node of the canvas, removes the current 
     * transform, decreases the current zoom by 5%, and applies the zoom to the
     * pane.
     * @see #applyZoom(Parent, double) 
     */
    public void zoomOut(){
        Parent parent = canvas.getParent();
        parent.getTransforms().remove(zoomScale);
        currentZoom -= 0.05;
        applyZoom(parent, currentZoom);
        if(currentZoom <= 0.05){
            isZoomedOut = true;
        }
        else{
            isZoomedOut = false;
        }
    }
    /**
     * Function to zoom to X percent.
     * This function gets the parent of the canvas, removes the current 
     * transform, and adds the desired zoom level to the pane.
     * @param zoom Desired zoom level. 
     */
    public void zoomToX(double zoom){
        Parent parent = canvas.getParent();
        zoom/=100;
        parent.getTransforms().remove(zoomScale);
        currentZoom = zoom;
        applyZoom(parent, currentZoom);
    }
    /**
     * Rotates the canvas 90 degrees. Adds it to the transformation pane.
     */
    public void rotate(){
        Rotate rotate = new Rotate(90,0,0);
        Main.paintController.getStaticPane().getTransforms().add(rotate);
    }
    /**
     * Getter for the canvas;
     * @return Canvas of the object
     */
    public Canvas getCanvas(){
        return canvas;
    }
    /**
     * Getter for the opened file.
     * @return Opened file of the object
     */
    public File getOpenedFile(){
        return openedFile;
    }
    /**
     * Getter for the saved file.
     * @return Saved file of the object
     */
    public File getSavedFile(){
        return savedFile;
    }
    /**
     * Getter for a file extension of a given file. 
     * @param file File to get the extension of
     * @return String extension
     */
    public String getFileExtension(File file){
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            //ystem.out.println(fileName.substring(fileName.lastIndexOf(".") + 1));
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
    /**
     * Getter for the saved state of the canvas.
     * @return Saved state of the canvas
     */
    public boolean getHasBeenModified(){
        return hasBeenModified;                
    }
    /**
     * Getter for isZoomedOut.
     * @return True if currentZoom is less than 0.05. False otherwise.
     */
    public boolean getIsZoomedOut(){
        return isZoomedOut;                
    }
    /**
     * Getter for the rectangle
     * @return BetterRectangle object
     */
    public BetterRectangle getRectangle(){
        return rectangle;
    }
    /**
     * Getter for the drawing tool.
     * @return DrawTool
     */
    public DrawingTools getDrawingTool(){
        return drawTools;
    }
    /**
     * Getter for the number of sides
     * @return Number of sides
     */
    public int getNumSides(){
        return numSides;
    }
    /**
     * Getter for the current fill color
     * @return Fill Color
     */
    public Color getFillColor(){
        return fillColor;
    }
    /**
     * Getter for the graphics context.
     * @return GraphicsContext for the object
     */
    public GraphicsContext getGraphicsContext(){
        return gc;
    }
    /**
     * Getter for the current zoom.
     * @return Current zoom of the object.
     */
    public double getCurrentZoom(){
        return currentZoom;
    }
    /**
     * Getter for the current selection
     * @return Current Selection
     */
    public SelectionRectangle getSelectionRecatangle(){
        return selection;
    }
    /**
     * Setter for the drawing mode
     * @param mode Desired drawing mode
     */
    /**
     * Setter for the saved file of the canvas
     * @param file Desired save location
     */
    public void setSavedFile(File file){
        this.savedFile = file;
    }
    /**
     * Setter for the saved state of the canvas.
     * @param bool Changed state of the canvas. True if modified, false if saved
     */
    public void setHasBeenModified(boolean bool){
        this.hasBeenModified = bool;               
    }
    /**
     * Setter for the text to be drawn
     * @param text Desired text to be drawn
     */
    public void setUserText(String text){
       this.userText = text;
    }
    
    /**
     * Sets the mode of the canvas.
     * @param type Type of tool listed in DrawingTools.java
     */
    public void setDrawingTool(DrawingTools type){
        this.drawTools=type;
    }
    /**
     * Sets the current selection of the canvas to null.
     */
    public void setCurrentSelection(ImageView iv){
        currentSelection = iv;
    }
    public void setDrawingMode(DrawingMode mode){
        this.drawMode=mode;
    }
    /**
     * Sets the redrawn image of the canvas
     * @param image Image to be held in redrawn image
     */
    public void setRedrawnImage(Image image){
        this.redrawnImage = image;
    }
    /**
     * Sets the line width of the graphics context.
     * @param width Desired line width
     */
    public void setLineWidth(double width){
        gc.setLineWidth(width);
    }
    /**
     * Sets number of sides for Polygon object.
     * @param sides Desired number of sides for polygon object
     */
    public void setNumSides(int sides){
        numSides = sides;
    }
    /**
     * Sets the fill color of the graphics context.
     * @param color Desired fill color
     */
    public void setFillColor(Color color){
        this.fillColor = color;
        gc.setFill(color);
    }
    /**
     * Sets the stroke color for the graphics context
     * @param color Desired stroke color
     */
    public void setStrokeColor(Color color){
        this.strokeColor = color;
        gc.setStroke(color);
    }
    /**
     * Sets the font size.
     * This function is not used.
     * @param size Desired size of the font
     * @deprecated 
     */
    public void setFontSize(int size){
        this.fontSize=size;
    }
    /**
     * Sets the font of the canvas
     * @param font Desired font. This includes font family and size
     */
    public void setFont(Font font){
        this.font = font;
        gc.setFont(font);
    }
    
}
