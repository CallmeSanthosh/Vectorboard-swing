package controller;

import java.awt.*;
import java.util.*;

import model.LineModel;
import model.ShapeModel;
import view.Canvas;

public class LineController extends ShapeController {
    public LineController(ShapeModel model, Canvas canvas) {
        super(model, canvas);
    } //End DLine constructor

    @Override
    public void draw(Graphics g, boolean selected) {
        LineModel line = getModel();
        g.setColor(getColor());
        g.drawLine(line.getPoint1().x, line.getPoint1().y, line.getPoint2().x, line.getPoint2().y);
        if(selected) {
            drawKnobs(g);
        }
    } //End draw

    @Override
    public LineModel getModel() {
        return (LineModel) model;
    } //End getModel

    public ArrayList<Point> getKnobs() {
        if(knobs == null || needsRecomputeKnobs) {
            knobs = new ArrayList<Point>();
            LineModel line = (LineModel) model;
            knobs.add(new Point(line.getPoint1()));
            knobs.add(new Point(line.getPoint2()));
        }
        return knobs;
    } //End getKnobs
}
