package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Star object. This object extends {@link Polygon}
 * @author Dylan
 * @since 4.0
 */
public class Star extends Polygon {
    /**
     * Constructor for the star object.
     * @param x X location. 
     * @param y Y location. 
     */
    public Star(double x, double y) {
        super(x,y);
    }
    /**
     * Override for the draw function from {@link PaintShape}.
     * This function uses the mathematical formula for any n-sided polygon.
     * It sets the angle to 2Pi/5, has 5 sides, and steps the angle by triple
     * the previous angle plus Pi. 
     * @param gc Graphics
     * @param sides 5 sides. Called in the PaintCanvas class.  
     */
    @Override
    public void draw(GraphicsContext gc, int sides){
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;   
        final double angleStep = Math.PI*2/5;
        double angle = 0;
        double radiusDistance = Math.sqrt(Math.pow((x1-x0),2)+Math.pow((y1-y0),2));
        double[] xPoints = new double[5];
        double[] yPoints = new double[5];
        for(int i=0; i < 5; i++, angle+=angleStep){
            xPoints[i] = Math.sin(angle*3+Math.PI)*radiusDistance+x0;
            yPoints[i] = Math.cos(angle*3+Math.PI)*radiusDistance+y0;
        }
        gc.fillPolygon(xPoints, yPoints, 5);
        gc.strokePolygon(xPoints, yPoints, 5);
        
    }
}
