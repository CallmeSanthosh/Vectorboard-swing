package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import model.EllipseModel;
import model.LineModel;
import model.RectModel;
import model.ShapeModel;
import view.Canvas;
import view.Whiteboard;

public class ShapeTest {

	private ShapeModel model;
	private Canvas canvas;
	private Whiteboard Whiteboard;
	Point point;
	private ArrayList<ShapeModel> modelShapes;

	@Before
	public void setup() {    
		canvas = new Canvas(Whiteboard);
		modelShapes = new ArrayList<ShapeModel>();
		//add rectangle
		model = new RectModel();
	}

	@Test
	public void modelExists() {
		assertNotNull(model);
	}
	
	@Test
	public void testEmptyShapesArray() {
		assertEquals(0, canvas.getShapeModels().size());
	}

	@Test
	public void addShapeRectangleTest() {
		model = new RectModel();
		model.setBounds(0, 0, 20, 20);
		canvas.addShape(model);
		assertEquals(1, canvas.getShapeModels().size());
		assertTrue(canvas.getShapeModels().get(0) instanceof RectModel);
	}
	
	@Test
	public void addShapeEllipseTest() {
		model = new EllipseModel();
		model.setBounds(140, 140, 30, 30);
		canvas.addShape(model);
		assertEquals(1, canvas.getShapeModels().size());
		assertTrue(canvas.getShapeModels().get(0) instanceof EllipseModel);
	}
	
	@Test
	public void addShapeLineTest() {
		model = new LineModel();
		model.setBounds(280, 280, 20, 20);
		canvas.addShape(model);
		assertEquals(1, canvas.getShapeModels().size());
		assertTrue(canvas.getShapeModels().get(0) instanceof LineModel);
	}

	@Test
	public void addMoreShapesTest() {
		model = new RectModel();
		model.setBounds(0, 0, 20, 20);
		canvas.addShape(model);
		model = new EllipseModel();
		model.setBounds(140, 140, 30, 30);
		canvas.addShape(model);
		model = new LineModel();
		model.setBounds(280, 280, 20, 20);
		canvas.addShape(model);
		assertEquals(3, canvas.getShapeModels().size());
		//models are added on last in first out basis
		assertTrue(canvas.getShapeModels().get(0) instanceof LineModel);
		assertTrue(canvas.getShapeModels().get(1) instanceof EllipseModel);
		assertTrue(canvas.getShapeModels().get(2) instanceof RectModel);
	}

	@Test
	public void undoTest() {
		model = new RectModel();
		model.setBounds(0, 0, 20, 20);
		canvas.addShape(model);
		model = new EllipseModel();
		model.setBounds(140, 140, 30, 30);
		canvas.addShape(model);
		model = new LineModel();
		model.setBounds(280, 280, 20, 20);
		canvas.addShape(model);
		assertEquals(3, canvas.getShapeModels().size());
		canvas.undoShapes();
		assertEquals(2, canvas.getShapeModels().size());
		assertEquals(1, canvas.getClearedShapesModels().size());
		assertTrue(canvas.getShapeModels().get(0) instanceof EllipseModel);
		assertTrue(canvas.getShapeModels().get(1) instanceof RectModel);
		canvas.undoShapes();
		assertEquals(1, canvas.getShapeModels().size());
		assertEquals(2, canvas.getClearedShapesModels().size());
		assertTrue(canvas.getShapeModels().get(0) instanceof RectModel);
		canvas.undoShapes();
		assertEquals(0, canvas.getShapeModels().size());
		assertEquals(3, canvas.getClearedShapesModels().size());
	}
	    @Test
	    public void redoTest() {
	    	model = new RectModel();
			model.setBounds(0, 0, 20, 20);
			canvas.addShape(model);
			model = new EllipseModel();
			model.setBounds(140, 140, 30, 30);
			canvas.addShape(model);
			model = new LineModel();
			model.setBounds(280, 280, 20, 20);
			canvas.addShape(model);
			canvas.undoShapes();
			canvas.undoShapes();
			assertEquals(1, canvas.getShapeModels().size());
			assertEquals(2, canvas.getClearedShapesModels().size());
			assertTrue(canvas.getShapeModels().get(0) instanceof RectModel);
			canvas.redoShapes();
	        assertEquals(2, canvas.getShapeModels().size());
	        assertEquals(1, canvas.getClearedShapesModels().size());
	        assertTrue(canvas.getShapeModels().get(0) instanceof EllipseModel);
			canvas.redoShapes();
	        assertEquals(3, canvas.getShapeModels().size());
	        assertEquals(0, canvas.getClearedShapesModels().size());
	        assertTrue(canvas.getShapeModels().get(0) instanceof LineModel);
	    }
	    @Test
	    public void clearTest() {
	    	model = new RectModel();
			model.setBounds(0, 0, 20, 20);
			canvas.addShape(model);
			model = new EllipseModel();
			model.setBounds(140, 140, 30, 30);
			canvas.addShape(model);
			model = new LineModel();
			model.setBounds(280, 280, 20, 20);
			canvas.addShape(model);
	        assertEquals(3, canvas.getShapeModels().size());
	        canvas.clear();
	        assertEquals(0, canvas.getShapeModels().size());
	        assertEquals(0, canvas.getClearedShapesModels().size());
	    }
	    
}
