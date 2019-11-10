package paint_overhaul.shapes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Text object. This extends {@link PaintShape}. 
 * @author Dylan
 * @since 4.0
 */
public class Text extends PaintShape{
    private String text;
    /**
     * Constructor for the text. 
     * @param x X location.
     * @param y Y location. 
     * @param text String to be printed.
     */
    public Text(double x, double y, String text) {
        super(x, y, x, y);
        this.text = text;
    }
    /**
     * Override for the abstract draw function in PaintShape. Fills the text
     * with the color from the fillColorPicker. 
     * @param gc GraphicsContext of the PaintCanvas. 
     */
    @Override
    public void draw(GraphicsContext gc){
        gc.fillText(text, x1, y1);
        //gc.strokeText(text, x1, y1);
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
