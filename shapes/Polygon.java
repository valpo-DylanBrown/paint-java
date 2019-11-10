package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Polygon object. This extends {@link PaintShape}
 * @author dylan
 * @since 4.0
 */
public class Polygon extends PaintShape{
    private double rotation;
    /**
     * Polygon Constructor. Sets the isPolygon flag to true. Sets the rotation
     * to Pi.
     * @param x X location. 
     * @param y Y location. 
     */
    public Polygon(double x, double y) {
        super(x,y,x,y);
        setIsPolygon();
        setRotation(Math.PI);
    }
    /**
     * Override for the setEnd function. 
     * @param x1 Ending X location.
     * @param y1 Ending Y location. 
     */
    @Override
    public void setEnd(double x1, double y1) {
        super.setEnd(x1,y1);
    }
    /**
     * Override for draw function in PaintShape. Uses the mathematical formula
     * for any regular polygon. 
     * @param gc GraphicsContext for the PaintCanvas. 
     * @param sides Desired number of sides for the polygon. 
     */
    @Override
    public void draw(GraphicsContext gc, int sides){
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;   
        final double angleStep = Math.PI*2/sides;
        double angle = 0;
        double radiusDistance = Math.sqrt(Math.pow((x1-x0),2)+Math.pow((y1-y0),2));
        double[] xPoints = new double[sides];
        double[] yPoints = new double[sides];
        for(int i=0; i < sides; i++, angle+=angleStep){
            xPoints[i] = Math.sin(angle+rotation)*radiusDistance+x0;
            yPoints[i] = Math.cos(angle+rotation)*radiusDistance+y0;
        }
        gc.fillPolygon(xPoints, yPoints, sides);
        gc.strokePolygon(xPoints, yPoints, sides);
        
    }
    /**
     * Drawing function for regular shapes. 
     * @param gc GraphicsContext for the PaintCanvas.
     * @deprecated  
     */
    @Override
    public void draw(GraphicsContext gc){}
    @Override
    public final void setIsPolygon(){
        isPolygon = true;
    }
    public final void setRotation(double num){
        rotation = num;
    }
}
