package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import util.ModelListener;

public abstract class ShapeModel {
    protected ArrayList<ModelListener> listeners;
    protected Rectangle bounds;
    protected Color color;
    protected int shapeID;
    protected boolean removeModel;

    public ShapeModel() {
        this(0, 0);
    }

    public ShapeModel(int x, int y) {
        this(x, y, 0, 0, Color.gray);
    }

    public ShapeModel(int x, int y, int width, int height, Color color) {
        bounds = new Rectangle(x, y, width, height);
        this.color = color;
        listeners = new ArrayList<ModelListener>();
        removeModel = false;
    } //End DShapeModel constructor

    //Get the current location of the model
    public Point getLocation() {
        return bounds.getLocation();
    }

    //Set location based on coordinates
    public void setLocation(int x, int y) {
        bounds.setLocation(x, y);
        notifyListeners();
    }

    //Set location based on the point
    public void setLocation(Point pt) {
        setLocation(pt.x, pt.y);
    }

    //Moves the points
    public void move(int dx, int dy) {
        bounds.x += dx;
        bounds.y += dy;
        notifyListeners();
    } //End move

    public void modifyWithPoints(Point anchorPoint, Point movingPoint) {
        int x;
        int y;
        if(anchorPoint.x < movingPoint.x) {
            x = anchorPoint.x;
        }
        else {
            x = movingPoint.x;
        }
        if(anchorPoint.y < movingPoint.y) {
            y = anchorPoint.y;
        } else {
            y = movingPoint.y;
        }
        int width = Math.abs(anchorPoint.x - movingPoint.x);
        int height = Math.abs(anchorPoint.y - movingPoint.y);
        setBounds(new Rectangle(x, y, width, height));
    } //End modifyWithPoints

    //Return the bounds of the shape model
    public Rectangle getBounds() {
        return bounds;
    }

    public int getID() {
        return shapeID;
    }

    //Sets the bounds with given x and coordinates, width and height
    public void setBounds(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
        notifyListeners();
    }

    //Sets the bounds with the given point, width, and height
    public void setBounds(Point pt, int width, int height) {
        bounds = new Rectangle(pt.x, pt.y, width, height);
        notifyListeners();
    }

    //Sets the bounds of the rectangle
    public void setBounds(Rectangle newBounds) {
        bounds = new Rectangle(newBounds);
        notifyListeners();
    }

    public void setID(int newID) {
        shapeID = newID;
    }

    //Set the color of the model
    public void setColor(Color color) {
        this.color = color;
        notifyListeners();
    }

    //Returns the color of the model
    public Color getColor() {
        return color;
    }

    public void removeCorrespondingShape() {
        removeModel = true;
        notifyListeners();
    }

    //Adds the current listener object to the list
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    //Remove corresponding listener object from the list
    public boolean removeListener(ModelListener listener) {
        return listeners.remove(listener);
    }

    public void mimic(ShapeModel other) {
        setBounds(other.getBounds());
        setColor(other.getColor());
        setID(other.getID());
        notifyListeners();
    }

    public boolean modelRemoved() {
        return removeModel;
    }

    //Notify the listener if the model has changed
    public void notifyListeners() {
        for(ModelListener listener : listeners) {
            listener.modelChanged(this);
        }
    }

    public int getX() {
        return bounds.x;
    }
    public int getY() {
        return bounds.y;
    }

    public int getHeight() {
        return bounds.height;
    }

    public int getWidth() {
        return bounds.width;
    }
} //End DShapeModel