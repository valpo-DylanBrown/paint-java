package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Ellipse object. This class extends {@link PaintShape}
 * @author dylan
 * @since 4.0
 */
public class Ellipse extends PaintShape {
    /**
     * 4-input constructor for Ellipse object. 
     * @param startX Starting X location. 
     * @param startY Starting Y location. 
     * @param endX Ending X location. 
     * @param endY Ending Y location. 
     */
    public Ellipse(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }
    /**
     * 2-input constructor for Ellipse. 
     * @param x X location.
     * @param y Y location. 
     */
    public Ellipse(double x, double y) {
        super(x,y,x,y);
    }
    /**
     * Override for draw function in PaintShape. Handles checking for shapes
     * being drawn the opposite way. Then fills and strokes the ellipse. 
     * @param gc GraphicsContext for the PaintCanvas. 
     */
    @Override
    public void draw(GraphicsContext gc){
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        gc.fillOval(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);
        gc.strokeOval(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);
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
