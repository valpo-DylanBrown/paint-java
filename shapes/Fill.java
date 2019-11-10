package paint_overhaul.shapes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Fill object.
 * @author Dylan
 */
public class Fill extends PaintShape{
    private Canvas canvas;
    public Fill(double x, double y, Canvas canvas) {
        super(x,y,x,y);
        this.canvas = canvas;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }
    @Override
    public void draw(GraphicsContext gc, int sides) {}

}
