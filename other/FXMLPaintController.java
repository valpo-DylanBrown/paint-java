package paint_overhaul.other;

//BIG TODO SPLIT THINGS UP INTO FUNCTIONS AND DIFFERENT FILES

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import paint_overhaul.constant.*;
import paint_overhaul.alerts.*;
import paint_overhaul.threads.AutoSaveThread;
/**
 * <p>
 * FXMLPaintController is the base class for the whole program.
 * This class is the controller for pain(t). It implements all functions needed to create a functioning
 * version of MS Paint. 
 * This class references all tags created in the .fxml file and creates local variables to store data in.
 * This class also references all actions created in the .fxml file. 
 * </p>
 * @author dylan
 * @since 1.0
 */
public class FXMLPaintController extends DefaultController {
    /*
    FXML vars
    */
    @FXML private BorderPane borderPane;
    @FXML private Pane staticPane;
    @FXML private Canvas canvas;
    @FXML private ColorPicker strokeColorPicker;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ToggleButton drawButton;
    @FXML private ToggleButton lineButton;
    @FXML private ToggleButton fillButton;
    @FXML private ToggleButton eraserButton;
    @FXML private ToggleButton rectButton;
    @FXML private ToggleButton squareButton;
    @FXML private ToggleButton ovalButton;
    @FXML private ToggleButton circleButton;
    @FXML private ToggleButton triangleButton;
    @FXML private ToggleButton polygonButton;
    @FXML private ToggleButton starButton;
    @FXML private ToggleButton textButton;
    @FXML private ToggleButton eyedropperButton;
    @FXML private ToggleButton selectionRectangle;
    @FXML private Button zoomInButton;
    @FXML private Button zoomOutButton;
    @FXML private Label zoomLabel;
    @FXML private Label autoSaveLabel; 
    @FXML private Label toolStatus;
    @FXML private Button closeButton;
    @FXML private Button undoButton;
    @FXML private Button redoButton;
    @FXML private MenuItem saveAsMenu;
    @FXML private ComboBox<String> fontChooser;
    @FXML private Spinner fontSizeSpinner;
    @FXML private TextField userText;
    @FXML private Spinner polygonSpinner;
    @FXML private Spinner widthSpinner;
    /*
    Non-FXML vars
    */
    private boolean isVisible = true;
    private ReleaseNotesAlert releaseNotesAlert;
    private LogAlert logAlert;
    private HelpAlert helpAlert;
    private AboutAlert aboutAlert;
    private SmartSaveAlert smartSaveAlert;
    private ResizeCanvasAlert resizeCanvasAlert;
    private PaintCanvas paintCanvas;
    private LossWarning lossWarning;
    private BetterFileChooser openFileChooser;
    private BetterFileChooser saveFileChooser;
    
    /**
     * Constructor for Object.
     */
    public FXMLPaintController(){
        Main.paintController = this;
    }
    /**
     * Getter for the object. 
     * @return PaintCanvas object. 
     */
    public PaintCanvas getPaintCanvas(){
        return paintCanvas;
    }
    /**
     * Getter for the thread of the controller
     * @return autoSaveThread
     */
    public AutoSaveThread getAutoSaveThread(){
        return autoSaveThread;
    }
    
    /**
     * FXML Function to handle opening a file. 
     * This function opens a file using the file chooser,
     * loads it into the canvas, and enables the zoom buttons.
     * @see paint_overhaul.other.BetterFileChooser#getFileChooser() 
     * @see paint_overhaul.other.PaintCanvas#loadImageFromFille(File) 
     */
    @FXML
    public void handleOpen(){
        File fileToOpen = openFileChooser.getFileChooser().showOpenDialog(borderPane.getScene().getWindow());
        if(fileToOpen == null){
            return;
        }
        paintCanvas.loadImageFromFille(fileToOpen);
        try {
            autoSaveThread.logFile(fileToOpen.getName(), true);
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
        zoomInButton.setDisable(false);
        zoomOutButton.setDisable(false);
    }
    /**
     * FXML Function to handle saving a file. 
     * If a file hasn't been saved before, the function will fire the save as 
     * button. Otherwise, it saves the image to the previously saved file.
     * @see #handleSaveAs()
     * @see paint_overhaul.other.PaintCanvas#saveCanvasToFile(File) 
     */
    @FXML
    public void handleSave(){
        if(paintCanvas.getSavedFile() == null){
            saveAsMenu.fire();
            return;
        }
        paintCanvas.saveCanvasToFile(paintCanvas.getSavedFile());
        
        try {
            autoSaveThread.logFile(paintCanvas.getSavedFile().getName(), false);
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * FXML Function to handle save as functionality. 
     * Opens the file chooser to get a location for the file. Then, the file is
     * saved to the location. 
     * @see paint_overhaul.other.BetterFileChooser#getFileChooser() 
     * @see paint_overhaul.other.PaintCanvas#saveCanvasToFile(File) 
     */
    @FXML
    public void handleSaveAs(){
        File savedFile = saveFileChooser.getFileChooser().showSaveDialog(borderPane.getScene().getWindow());
        if(savedFile==null){
            return;
        }
        paintCanvas.saveCanvasToFile(savedFile);
        
        try {
            autoSaveThread.logFile(savedFile.getName(), false);
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * FXML Function to close the application from File-&gt;Exit.
     * Fires the close button
     * @see #handleCloseButton()
     */
    @FXML
    public void handleExit(){
        closeButton.fire();
    }
    /**
     * Function to handle alerts from Help-&gt;About.
     * @see paint_overhaul.alerts.AboutAlert#createAboutAlert() 
     */
    @FXML
    public void handleAbout(){
        aboutAlert.createAboutAlert();
    }
    /**
     * Function to handle alerts from Help-&gt;Release Notes.
     * @see paint_overhaul.alerts.ReleaseNotesAlert#createAlert()
     */
    @FXML 
    public void handleReleaseNotes(){
        releaseNotesAlert.createAlert();
    }
    @FXML 
    public void handleLog(){
        logAlert.createAlert();
    }
    /**
     * Function to handle alerts from Help-&gt;Help Contents.
     * @see paint_overhaul.alerts.HelpAlert#createAlert()
     */
     
    @FXML 
    public void handleHelpContents(){
        helpAlert.createAlert();
    }
    @FXML
    public void handleCopyButton(){
        paintCanvas.getSelectionRecatangle().setCopyMode(true);
    }
    /**
     * FXML Function to undo the last action from Edit-&gt;Undo.
     * Fires the undo button.
     * @see #handleUndoButton() 
     */
    @FXML
    public void handleUndoMenuItem(){
        undoButton.fire();
    }
    /**
     * FXML Function to undo the last action from Edit-&gt;Redo.
     * Fires the redo button.
     * @see #handleRedoButton() 
     */
    @FXML
    public void handleRedoMenuItem(){
        redoButton.fire();
    }
    /**
     * FXML Function to create an alert for resizing the canvas.
     * @see paint_overhaul.alerts.ResizeCanvasAlert#createResizeDialog() 
     */
    @FXML
    public void handleResizeCanvas(){
        resizeCanvasAlert.createResizeDialog();
    }
    /**
     * Undoes the last action.
     * @see paint_overhaul.other.PaintCanvas#undoLast() 
     */
    @FXML
    public void handleUndoButton(){
        paintCanvas.undoLast();
    }
    /**
     * Re-does the last action.
     * @see paint_overhaul.other.PaintCanvas#redoLast() 
     */
    @FXML
    public void handleRedoButton(){
        paintCanvas.redoLast();
    }
    /**
     * Sets the font of the text tool.
     * @see paint_overhaul.other.PaintCanvas#setFont(Font) 
     */
    @FXML
    public void handleFontChooser(){
        paintCanvas.setFont(new Font(fontChooser.getValue(), (int)fontSizeSpinner.getValue()));
    }
    /**
     * Sets the stroke color for tools.
     * @see paint_overhaul.other.PaintCanvas#setStrokeColor(Color) 
     */
    @FXML
    public void handleStrokeColorPicker(){
        paintCanvas.setStrokeColor(strokeColorPicker.getValue());
    }
    /**
     * Sets the fill color for tools.
     * @see paint_overhaul.other.PaintCanvas#setFillColor(Color) 
     */
    @FXML
    public void handleFillColorPicker(){
        paintCanvas.setFillColor(fillColorPicker.getValue());
    }
    /**
     * Function to swap colors between fill and stroke.
     * Gets the current color on the fill and saves it in a temp location.
     * Then, sets the fill color to the stroke color, and sets the stroke color
     * to the fill color. 
     */
    @FXML
    private void handleSwapColorButton(){
        Color tempColor = fillColorPicker.getValue();
        fillColorPicker.setValue(strokeColorPicker.getValue());
        paintCanvas.setFillColor(fillColorPicker.getValue());
        strokeColorPicker.setValue(tempColor);
        paintCanvas.setStrokeColor(strokeColorPicker.getValue());
        tempColor = null; //destroy tempColor to save memory
    }
    /**
     * Logic to handle the application close button. If the canvas
     * has been modified, the button opens the smart save alert. Otherwise,
     * the program cleanly exits. 
     * @see paint_overhaul.alerts.SmartSaveAlert
     */
    @FXML
    private void handleCloseButton(){
        if(paintCanvas.getHasBeenModified()==true){
            smartSaveAlert.setSmartSaveAlert();
        }
        else{
            Platform.exit();
        }
    }
    /**
     * Function to toggle visibility of auto-save label. 
     */
    @FXML
    public void handleAutoSaveLabel(){
        if(isVisible == true){
            autoSaveLabel.setVisible(false);
            isVisible = false;
        }
        else{
            autoSaveLabel.setVisible(true);
            isVisible = true;
        }
    }
    /**
     * Rotates the canvas.
     * @see paint_overhaul.other.PaintCanvas#rotate() 
     */
    @FXML
    public void handleRotate(){
        paintCanvas.rotate();
    }
    /**
     * Zooms the canvas in.
     * @see paint_overhaul.other.PaintCanvas#zoomIn() 
     */
    @FXML
    public void handleZoomInButton(){
        paintCanvas.zoomIn();
        zoomOutButton.setDisable(false);
        updateZoomLabel();
    }
    /**
     * Zooms the canvas out.
     * @see paint_overhaul.other.PaintCanvas#zoomOut() 
     */
    @FXML
    public void handleZoomOutButton(){
        paintCanvas.zoomOut();
        if(paintCanvas.getIsZoomedOut() == true){
            zoomOutButton.setDisable(true);
        }
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * Fires zoomInButton.
     */
    @FXML
    public void handleZoomInMenu(){
        handleZoomInButton();
    }
    /**
     * Zoom menu option.
     * Fires zoomOutButton.
     */
    @FXML
    public void handleZoomOutMenu(){
        handleZoomOutButton();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom25Menu(){
        paintCanvas.zoomToX(25);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom50Menu(){
        paintCanvas.zoomToX(50);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom75Menu(){
        paintCanvas.zoomToX(75);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom100Menu(){
        paintCanvas.zoomToX(100);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom200Menu(){
        paintCanvas.zoomToX(200);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom250Menu(){
        paintCanvas.zoomToX(250);
        updateZoomLabel();
    }
    /**
     * Zoom menu option.
     * @see paint_overhaul.other.PaintCanvas#zoomOut()
     */
    @FXML
    public void handleZoom300Menu(){
        paintCanvas.zoomToX(300);
        updateZoomLabel();
    }
    /**
     * Updates the zoom label. Shows user the current level of zoom.
     * @see paint_overhaul.other.PaintCanvas#getCurrentZoom() 
     */
    public void updateZoomLabel(){
        zoomLabel.setText(Math.round(paintCanvas.getCurrentZoom()*100)+"% ");
    }
    /**
     * Updates the timer label, if visible, on the bottom bar 
     * @param string Current Time
     */
    public void setAutoSaveLabel(String string){
        autoSaveLabel.setText(string);
    }
    /**
     * Updates the current tool label.
     * @param string Tool selected
     */
    public void setToolStatus(String string){
        toolStatus.setText(string);
    }
    /**
     * Function to select the pencil tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleDrawToggle(){
        if(drawButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.PENCIL);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the star tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleStarButton(){
        if(starButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.STAR);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());

        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the line tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleLineToggle(){
        if(lineButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.LINE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the fill tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    public void handleFillToggle(){
        if(fillButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.FILL);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the eraser tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    public void handleEraserToggle(){
        if(eraserButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.ERASER);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the rectangle tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleRectToggle(){
        if(rectButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.RECTANGLE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the circle tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleCircleToggle(){
       if(circleButton.isSelected()){
           paintCanvas.setDrawingMode(DrawingMode.DRAW);
           paintCanvas.setDrawingTool(DrawingTools.CIRCLE);
           setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the square tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleSquareToggle(){
        if(squareButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.SQUARE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the ellipse tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleOvalToggle(){
        if(ovalButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.ELLIPSE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the triangle tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleTriangleToggle(){
        if(triangleButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.TRIANGLE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the polygon tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handlePolygonButton(){
        if(polygonButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.POLYGON);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the text tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleTextToggle(){
        if(textButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.TEXT);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the eyedropper tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleEDToggle(){
        if(eyedropperButton.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.DRAW);
            paintCanvas.setDrawingTool(DrawingTools.EYEDROPPER);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Function to select the selection tool. It also sets
     * the tool status bar at the bottom of the screen. It then logs tool using 
     * the Auto-Save thread. 
     */
    @FXML
    public void handleSelectionRectangle(){
        if(selectionRectangle.isSelected()){
            paintCanvas.setDrawingMode(DrawingMode.SELECT);
            paintCanvas.setDrawingTool(DrawingTools.SELECT);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        else{
            paintCanvas.setDrawingTool(DrawingTools.NONE);
            setToolStatus("Tool Selected: " + paintCanvas.getDrawingTool().toString());
        }
        
        try {
            autoSaveThread.logTool(paintCanvas.getDrawingTool().toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Configures the width spinner object. Sets the values to only be integers.
     * It also formats the text and watches for users not hitting enter. It sets
     * the line width to the new value.
     * @param spinner Spinner that is being analyzed
     * @throws NumberFormatException in case user enters unexpected values
     */
    public void configureWidthSpinner(Spinner spinner) throws NumberFormatException{
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100));
        TextFormatter formatter = new TextFormatter(spinner.getValueFactory().getConverter(), spinner.getValueFactory().getValue());
        spinner.getEditor().setTextFormatter(formatter);
        // bidi-bind the values
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());
        spinner.valueProperty().addListener((e) -> paintCanvas.setLineWidth((int) spinner.getValue()));
    }
    /**
     * Configures the polygon spinner object. Sets the values to only be 
     * integers. It also formats the text and watches for users not hitting 
     * enter. It sets the number of polygon sides to the new value
     * @param spinner Spinner that is being analyzed
     * @throws NumberFormatException in case user enters unexpected values
     */
    public void configurePolygonSpinner(Spinner spinner) throws NumberFormatException{
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 99));
        TextFormatter formatter = new TextFormatter(spinner.getValueFactory().getConverter(), spinner.getValueFactory().getValue());
        spinner.getEditor().setTextFormatter(formatter);
        // bidi-bind the values
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());
        spinner.valueProperty().addListener((e) -> paintCanvas.setNumSides((int) spinner.getValue()));
    }
    /**
     * Configures the font size spinner object. Sets the values to only be 
     * integers. It also formats the text and watches for users not hitting 
     * enter. It sets the font size to the new value
     * @param spinner Spinner that is being analyzed
     * @throws NumberFormatException in case user enters unexpected values
     */
    public void configureFontSizeSpinner(Spinner spinner) throws NumberFormatException{
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        TextFormatter formatter = new TextFormatter(spinner.getValueFactory().getConverter(), spinner.getValueFactory().getValue());
        spinner.getEditor().setTextFormatter(formatter);
        // bidi-bind the values
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());
        spinner.valueProperty().addListener((e) -> paintCanvas.setFont(new Font(fontChooser.getValue(),(int) spinner.getValue())));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //slider.valueProperty().addListener((e) -> paintCanvas.setLineWidth(slider.getValue()));
        configurePolygonSpinner(polygonSpinner);
        configureWidthSpinner(widthSpinner);
        configureFontSizeSpinner(fontSizeSpinner);
        
        fontChooser.getItems().setAll(Font.getFamilies());
        fontChooser.getSelectionModel().selectFirst();
        userText.textProperty().addListener((observable, oldValue, newValue) -> {
            paintCanvas.setUserText(newValue);
        });
        openFileChooser = new BetterFileChooser("Open Image: ");
        saveFileChooser = new BetterFileChooser("Save Canvas: ");
        
        autoSaveThread = new AutoSaveThread(60);
        autoSaveThread.startAutoSaveThread();
        
        paintCanvas = new PaintCanvas(canvas);
        releaseNotesAlert = new ReleaseNotesAlert();
        logAlert = new LogAlert();
        helpAlert = new HelpAlert();
        aboutAlert = new AboutAlert();
        smartSaveAlert = new SmartSaveAlert(paintCanvas, saveFileChooser);
        resizeCanvasAlert = new ResizeCanvasAlert(paintCanvas);
        lossWarning = new LossWarning(paintCanvas);
    }
    /**
     * Getter for the stroke color picker.
     * @return Stroke Color Picker
     */
    public ColorPicker getStrokeColorPicker(){
        return strokeColorPicker;
    }
    /**
     * Getter for the fill color picker
     * @return Fill Color Picker
     */
    public ColorPicker getFillColorPicker(){
        return fillColorPicker;
    }
    /**
     * Getter for the Border Pane of the controller
     * @return Border Pane
     */
    public BorderPane getBorderPane(){
        return borderPane;
    }
    /**
     * Getter for the static grouping canvas pane
     * @return Grouping Pane
     */
    public Pane getStaticPane(){
        return staticPane;
    }
    /**
     * Getter for the loss warning
     * @return LossWarning object
     */
    public LossWarning getLossWarning(){
        return lossWarning;
    }
}  