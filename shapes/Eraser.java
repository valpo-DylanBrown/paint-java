package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Eraser object. This class extends {@link Pencil}.
 * @author Dylan
 */
public class Eraser extends Pencil {
    /**
     * Constructor for the Eraser. 
     * @param x X location. 
     * @param y Y location. 
     */
    public Eraser(double x, double y){
        super(x,y);
    }
    /**
     * Override for draw function in Pencil. Saves original
     * characteristics, sets the color to white, and multiplies the old line
     * width by 2. It then draws using the {@link Pencil#draw(GraphicsContext)}.
     * Lastly, it sets the original characteristics back to the graphics context.
     * @param gc GraphicsContext for the PaintCanvas. 
     */
    @Override
    public void draw(GraphicsContext gc){
        Paint colorBeforeErase = gc.getStroke();
        double lineWidthBefore = gc.getLineWidth();
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(lineWidthBefore*2);
        super.draw(gc);
        gc.setStroke(colorBeforeErase);
        gc.setLineWidth(lineWidthBefore);
    }
}
