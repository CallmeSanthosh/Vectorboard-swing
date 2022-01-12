package view;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;

import controller.LineController;
import controller.EllipseController;
import controller.RectController;
import controller.ShapeController;
import model.LineModel;
import model.EllipseModel;
import model.RectModel;
import model.ShapeModel;
import util.FileHandler;
import util.MessageNotification;
import util.ModelListener;

import java.awt.Graphics2D;

public class Canvas extends JPanel implements ModelListener {
    public static final int SIZE = 400;
    public static final int INITIAL_POSITION = 10;
    public static final int INITIAL_SIZE = 20;
    private static final Random rand = new Random();
    private LinkedList<ShapeController> shapes;
    private LinkedList<ShapeController> clearedShapes;
    private ArrayList<ShapeModel> modelShapes;
    private ShapeController selected;
    private Point movingPoint;
    private Point anchorPoint;
    private int lastX, lastY;
    private Whiteboard whiteboard;
    private FileHandler fws;

    public Canvas(Whiteboard board)
    {
        setMinimumSize(new Dimension(SIZE, SIZE));
        setPreferredSize(getMinimumSize());
        setBackground(Color.white);
        whiteboard = board;
        shapes = new LinkedList<ShapeController>();
        clearedShapes = new LinkedList<ShapeController>();
        modelShapes = new ArrayList<ShapeModel>();
        fws = new FileHandler(modelShapes, this);
        selected = null;
        movingPoint = null;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //If whiteboard is running as a server, enable object selection
                if(!whiteboard.runningAsClient()) {
                    selectObject(e.getPoint());
                }

            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
           public void mouseDragged(MouseEvent e) {
               if(!whiteboard.runningAsClient()) {
                   int dx = e.getX() - lastX;
                   int dy = e.getY() - lastY;
                   lastX = e.getX();
                   lastY = e.getY();
                   if(movingPoint != null) {
                       movingPoint.x += dx;
                       movingPoint.y += dy;
                       selected.modifyShapeWithPoints(anchorPoint, movingPoint);
                   } else if(selected != null) {
                       selected.move(dx, dy);
                   }
               }
           }
        });
    } //End Canvas constructor
    //Add shape to canvas
    public void addShape(ShapeModel model) {
    	
        if(whiteboard!=null && !whiteboard.runningAsClient()) {
            model.setID(Whiteboard.getNextID());
        }
        modelShapes.add(model);
        //Repaint where the previous shape was
        //This makes the new shape move to the front
        if(selected != null) {
            repaintShape(selected);
        }
        ShapeController shape = null;
        if(model instanceof RectModel) {
            shape = new RectController(model, this);
        }
        else if(model instanceof EllipseModel) {
            shape = new EllipseController(model, this);
        }
        else if(model instanceof LineModel) {
            shape = new LineController(model, this);
        }
        model.addListener(this);
        shapes.addFirst(shape);
        if(whiteboard!=null && !whiteboard.runningAsClient()) {
            selected = shape;
        }
        if(whiteboard!=null && whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.ADD, model);
        }
        repaintShape(shape);
    } //End addShape
    //Returns a pointer to the list shapes
    public LinkedList<ShapeController> getShapes() {
        return shapes;
    } //End getShapes
    //Returns an array list of all the shape models of the shapes on the canvas
    public ArrayList<ShapeModel> getShapeModels() {
        ArrayList<ShapeModel> models = new ArrayList<ShapeModel>();
        for(ShapeController shape : shapes)
            models.add(shape.getModel());
        return models;
    } //End getShapeModels
    
    public ArrayList<ShapeModel> getClearedShapesModels() {
        ArrayList<ShapeModel> models = new ArrayList<ShapeModel>();
        for(ShapeController shape : clearedShapes)
            models.add(shape.getModel());
        return models;
    } //End getShapeModels
    
    //Repaints an area specified by the passed bounds
    public void repaintArea(Rectangle bounds) {
        repaint(bounds);
    } //End repaintArea
    //Repaint the passed shape
    public void repaintShape(ShapeController shape) {
        if(shape == selected) {
            repaint(shape.getBigBounds());
        } else {
            repaint(shape.getBounds());
        }
    } //End repaintShape

    //Paints and draw the shapes on canvas
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
        for(ShapeController shape : shapes)
        {
            shape.draw(g2, (selected == shape));
        }
    } //End paintComponent

    //Select the object that contains the given point if it exists
    public void selectObject(Point pt) {
        lastX = pt.x;
        lastY = pt.y;
        movingPoint = null;
        anchorPoint = null;
        //Check if there's a shape/object selected with knobs
        if(selected != null) {
            for(Point point : selected.getKnobs()) {
                //If a knob is selected, create new moving point
                //Also obtain which knob (anchor points) is selected
                if(selected.selectedKnob(pt, point)) {
                    movingPoint = new Point(point);
                    anchorPoint = selected.getAnchorForSelectedKnob(point);
                    break;
                }
            }
        }
        //If there is no knob selected, check if an object is clicked
        if(movingPoint == null) {
            selected = null;
            for(ShapeController shape : shapes) {
                if(shape.containsPoint(pt))
                    selected = shape;
            }
        }
        //If current whiteboard is in server mode
        //Send message to the client table for selection update
        if(selected != null && whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.CHANGE, selected.getModel());
        }
        repaint();
    } //End selectObject

    public ShapeController getSelected() {
        return selected;
    } //End getSelected

    public void setRandomizedColor(Color c) {
        Random rand = new Random();
        for(int i = 0; i < shapes.size(); i++)
        {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r,g,b);
            int s = rand.nextInt(shapes.size());
            shapes.get(s).setColor(randomColor);
        }
        this.repaint();
    } //End setRandomizedColor

    public ShapeController getShapeID(int ID) {
        for(ShapeController shape : shapes) {
            if(shape.getModelID() == ID) {
                return shape;
            }
        }
        return null;
    } //End getShapeID

    public void modelChanged(ShapeModel model) {
        if(whiteboard.runningAsServer() && !model.modelRemoved()) {
            whiteboard.getServer().sendMessage(MessageNotification.CHANGE, model);
        }
    }

    void save(File file) {
        fws.save(file);
    }

    void open(File file) {
        fws.open(file);
    }

    void saveImageFile(File f) {
        //Removes the knob selection before drawing
        ShapeController lastSelected = selected;
        selected = null;
        fws.saveImage(f);
        selected = lastSelected;
    }

    public void clear() {
        //Clear everything from client
        if(whiteboard != null && whiteboard.runningAsServer()) {
            for(int i = 0; i < shapes.size(); i++) {
                whiteboard.getServer().sendMessage(MessageNotification.REMOVE, shapes.get(i).getModel());
            }
        }
        clearedShapes.clear();
        shapes.clear();
        modelShapes.clear();
        selected = null;
        repaint();
    }

    public void remove(ShapeController shape) {
        shapes.remove(shape);
        if(whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.REMOVE, selected.getModel());
        }
        repaint();
    }

    public void setColor(Color c) {
        if(selected == null) {
            return;
        }
        selected.setColor(c);
        this.repaint();
    }

    public void clientRedoShapes(ShapeModel model, ShapeController shape) {
        shapes.addFirst(shape);
        clearedShapes.removeFirst();
        if(whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.REDO, selected.getModel());
        }
        model.addListener(this);
        repaintShape(shape);
    }

    public void clientUndoShapes(ShapeModel model, ShapeController shape) {
        if(whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.UNDO, selected.getModel());
        }
        model.addListener(this);
        shapes.addFirst(shape);
        if(!whiteboard.runningAsClient()) {
            selected = shape;
        }
        if(whiteboard.runningAsServer()) {
            whiteboard.getServer().sendMessage(MessageNotification.ADD, model);
        }
        repaintShape(shape);
    }

    public void redoShapes() {
        if (! clearedShapes.isEmpty()) {
            shapes.addFirst(clearedShapes.removeFirst());
            repaint();
        }
    }

    public void undoShapes() {
        if (! shapes.isEmpty())
        {
            clearedShapes.addFirst(shapes.removeFirst());
            repaint();
        }
    }
} //End Canvas
