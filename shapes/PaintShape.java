package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract PaintShape. This class is the default shape that all other shapes
 * are extended from. 
 * @author dylan
 * @since 4.0
 */
public abstract class PaintShape {
    protected double x0, x1;
    protected double y0, y1;
    
    protected boolean isPolygon;
    /**
     * 4-input constructor for general shape. 
     * @param x0 Starting X location.
     * @param y0 Starting Y location.
     * @param x1 Ending X location.
     * @param y1 Ending Y location. 
     */
    public PaintShape(double x0, double y0, double x1, double y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }
    /**
     * Abstract draw function. Children must override.
     * @param gc GraphicsContext for the PaintCanvas
     */
    public abstract void draw(GraphicsContext gc);
    
    /**
     * Abstract draw function. Children must override.
     * @param gc GraphicsContext for the PaintCanvas
     * @param sides Number of sides for the polygon object. 
     */
    public abstract void draw(GraphicsContext gc, int sides);
    
    /**
     * Updates the final X and Y location. 
     * @param x Ending X location. 
     * @param y Ending Y location. 
     */
    public void setEnd(double x, double y) {
        this.x1 = x;
        this.y1 = y;
    }
    /**
     * Function to see if the current shape is a polygon.
     * @return true if polygon, false otherwise. 
     */
    public boolean getIsPolygon(){
        return isPolygon;
    }
    /**
     * Function to set is polygon. Always false unless overridden. 
     */
    public void setIsPolygon(){
        isPolygon = false; 
    }

}
