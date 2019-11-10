package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;
/**
 * Triangle object. This extends {@link PaintShape}. 
 * @author dylan
 * @since 4.0
 * @see PaintShape
 */
public class Triangle extends PaintShape {
    /**
     * 4-input constructor for Triangle shape. Uses the method defined in the 
     * parent class. 
     * @param startX Starting X location. 
     * @param startY Starting Y location.
     * @param endX Ending X location.
     * @param endY Ending Y location.
     * @see PaintShape#PaintShape(double, double, double, double) 
     */
    public Triangle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }
    /**
     * 2-input constructor for the Triangle shape. Uses method defined in
     * parent class.
     * @param x X location.
     * @param y Y location.
     * @see PaintShape#PaintShape(double, double, double, double) 
     */
    public Triangle(double x, double y) {
        super(x,y,x,y);
    }
    /**
     * Override for abstract draw function in PaintShape. This calculates the 
     * math for the last point of the triangle. It then creates a new polygon,
     * using the position. 
     * @param gc GraphicsContext for the PaintCanvas. 
     */
    @Override
    public void draw(GraphicsContext gc){
        double x2 = ((x0 + x1 + Math.sqrt(3)*(y0-y1))/2);
        double y2 = ((y0 + y1 + Math.sqrt(3)*(x1-x0))/2);
        
        gc.fillPolygon(new double[]{x0,x1,x2}, new double[]{y0,y1,y2}, 3);
        gc.strokePolygon(new double[]{x0,x1,x2}, new double[]{y0,y1,y2}, 3);
    }
    /**
     * Override for abstract draw function. Not used for this shape.
     * @param gc GraphicsContext of the PaintCanvas.
     * @param sides Number of sides desired.
     * @deprecated 
     */
    @Override
    public void draw(GraphicsContext gc, int sides){}
}
