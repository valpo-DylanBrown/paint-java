package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Square Object. This extends {@link BetterRectangle}. 
 * @author dylan
 * @since 4.0
 */
public class Square extends BetterRectangle {
    /**
     * 4-input constructor for Square object. 
     * @param x0 Starting X location. 
     * @param y0 Starting Y location. 
     * @param x1 Ending X location. 
     * @param y1 Ending Y location. 
     */
    public Square(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }
    /**
     * 2-input constructor for Ellipse. 
     * @param x X location.
     * @param y Y location. 
     */
    public Square(double x, double y) {
        this(x, y, x, y);
    }
    /**
     * Override for draw in PaintCanvas. This function handles users drawing 
     * square in any direction. It calculates the needed distance for the drawing
     * to be square. It then calls the {@link BetterRectangle#draw(GraphicsContext)}.
    */
    @Override
    public void draw(GraphicsContext gc) {
        double maxWidth = Math.min(Math.abs(x1-x0), Math.abs(y1-y0));
        if(Math.abs(y1-y0) > maxWidth)
            if(y1 < y0)
                y1 = y0 - maxWidth;
            else
                y1 = y0 + maxWidth;
        if(Math.abs(x1-x0) > maxWidth)
            if(x1 < x0)
                x1 = x0 - maxWidth;
            else
                x1 = x0 + maxWidth;
        super.draw(gc);
    }
}
