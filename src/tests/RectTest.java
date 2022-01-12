package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import model.RectModel;
import model.ShapeModel;
import view.Canvas;
import view.Whiteboard;

public class RectTest {
	
    private ShapeModel model;
    private Canvas canvas;
    private Whiteboard Whiteboard;
    Point point;
    private ArrayList<ShapeModel> modelShapes;
    
    @Before
    public void setup() {
        model = new RectModel();
        canvas = new Canvas(Whiteboard);
        point = new Point();
        point.x = 0;
        point.y = 0;
        modelShapes = new ArrayList<ShapeModel>();
        model.setBounds(point, 20, 20);
        canvas.addShape(model);
    }

    @Test
    public void RectExistsTest() {
        assertNotNull(canvas.getShapeModels().get(0));
        assertTrue(model instanceof RectModel);
    }
}