package paint_overhaul.shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Pencil object. This extends {@link PaintShape}
 * @author dylan
 * @since 4.0
 */
public class Pencil extends PaintShape {
    private List<Point2D> path;
    /**
     * Pencil constructor. Creates a new ArrayList of points. 
     * @param x X location. 
     * @param y Y location. 
     */
    public Pencil(double x, double y) {
        super(x,y,x,y);
        path = new ArrayList<>();
        path.add(new Point2D(x,y));
    }
    /**
     * Override for setting the endpoint of the line. 
     * This function uses the original method, but adds the point to the array. 
     * @param x1 X location. 
     * @param y1 Y location.
     */
    @Override
    public void setEnd(double x1, double y1) {
        super.setEnd(x1,y1);
        path.add(new Point2D(x1,y1));
    }
    /**
     * Override for the draw function in PaintShape. Draws each point in the
     * array. 
     * @param gc GraphicsContext for the PaintCanvas.
     */
    @Override
    public void draw(GraphicsContext gc){
        for(int i = 1; i < path.size(); i++) {
            Point2D from = path.get(i-1);
            Point2D to = path.get(i);
            gc.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
        }
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
