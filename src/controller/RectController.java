package controller;

import java.awt.Graphics;
import java.awt.Rectangle;
import model.RectModel;
import model.ShapeModel;
import view.Canvas;

public class RectController extends ShapeController {

    public RectController(ShapeModel model, Canvas canvas) {
        super(model, canvas);
    }

    public RectModel getModel() {
        return (RectModel) model;
    }

    public void draw(Graphics g, boolean selected) {
        g.setColor(model.getColor());
        Rectangle bounds = model.getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        //If shape is selected, draw the knobs for it
        if(selected) {
            drawKnobs(g);
        }
    }
} //End DRect