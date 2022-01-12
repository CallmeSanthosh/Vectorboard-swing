package controller;

import java.awt.Graphics;
import java.awt.Rectangle;
import model.EllipseModel;
import model.ShapeModel;
import view.Canvas;

public class EllipseController extends ShapeController
{
    public EllipseController(ShapeModel model, Canvas canvas) {
        super(model, canvas);
    }

    public EllipseModel getModel() {
        return (EllipseModel) model;
    }

    public void draw(Graphics g, boolean selected) {
        g.setColor(model.getColor());
        Rectangle bounds = model.getBounds();
        g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
        if(selected) drawKnobs(g);
    }
} //End DOval