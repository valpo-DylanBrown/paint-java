package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Circle object. This class extends {@link Ellipse}
 * @author dylan
 * @since 4.0
 */
public class Circle extends Ellipse {
    /**
     * 4-input Circle constructor. 

     * @param startX Starting X location. 
     * @param startY Starting Y location. 
     * @param endX Ending X location. 
     * @param endY Ending Y location. 
     * @see Ellipse#Ellipse(double, double, double, double)
     */
    public Circle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }
    /**
     * 2-input Circle constructor.
     * @param x X location. 
     * @param y Y location.
     * @see Ellipse#Ellipse(double, double) 
     */
    public Circle(double x, double y) {
        super(x,y,x,y);
    }
    /**
     * Override for the draw function from Ellipse. Sets the radius of the 
     * circle, and keeps it even. 
     * @param gc GraphicsContext for the PaintCanvas
     */
    @Override
    public void draw(GraphicsContext gc){
        double maxRadius = Math.min(Math.abs(x1-x0), Math.abs(y1-y0));
        if(Math.abs(y1-y0) > maxRadius)
            if(y1 < y0)
                y1 = y0 - maxRadius;
            else
                y1 = y0 + maxRadius;
        if(Math.abs(x1-x0) > maxRadius)
            if(x1 < x0)
                x1 = x0 - maxRadius;
            else
                x1 = x0 + maxRadius;
        super.draw(gc);
    }
}
