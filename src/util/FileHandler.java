package util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.*;

import model.ShapeModel;
import view.Canvas;

public class FileHandler {

    private ArrayList<ShapeModel> shapes;
    private Canvas c;

    public FileHandler(ArrayList<ShapeModel> shapes, Canvas c) {
        this.c = c;
        this.shapes = shapes;
    }

    public void save(File file) {
        try {
                XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
                xmlOut.writeObject(shapes);
                xmlOut.close();
        } catch(IOException e) {
                e.printStackTrace();
        }
    } //End save

    public void open(File file) {
        ArrayList<ShapeModel> s = null;
        try {
            XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            s = (ArrayList<ShapeModel>) xmlIn.readObject();
            xmlIn.close();
            c.clear();
            for(ShapeModel shapeModel: s) {
                c.addShape(shapeModel);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    } //End open

    public void saveImage(File file) {
        BufferedImage image = (BufferedImage) c.createImage(c.getWidth(), c.getHeight());
        Graphics g = image.getGraphics();
        c.paintAll(g);
        g.dispose();
        File pngFile = new File(file + ".PNG");
        try {
            ImageIO.write(image, "PNG", pngFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
} //End FileWriterAndSaver
