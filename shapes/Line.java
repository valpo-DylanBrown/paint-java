package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Line object. This object extends {@link PaintShape}
 * @author dylan
 */
public class Line extends PaintShape {
    /**
     * 4-input line constructor. 
     * @param startX Starting X location. 
     * @param startY Starting Y location. 
     * @param endX Ending X location. 
     * @param endY Ending Y location. 
     */
    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }
    /**
     * 2-input Line constructor. 
     * @param x X location. 
     * @param y Y location.
     */
    public Line(double x, double y) {
        super(x,y,x,y);
    }
    /**
     * Override for draw function in PaintShape. Strokes the line to the ending
     * location. 
     * @param gc GraphicsContext for the PaintCanvas. 
     */
    @Override
    public void draw(GraphicsContext gc){
        gc.strokeLine(x0, y0, x1, y1);
    }
    /**
     * Drawing function for polygon shapes. 
     * @param gc GraphicsContext for the PaintCanvas.
     * @param sides Number of sides desired.
     * @deprecated  
     */
    @Override
    public void draw(GraphicsContext gc, int sides){}

}
