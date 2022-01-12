package util;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import javax.swing.SwingUtilities;
import controller.ShapeController;
import view.Canvas;
import view.Whiteboard;

public class Client extends Thread
{
    private String host;
    private int port;
    private Whiteboard whiteboard;
    private Canvas canvas;

    public Client(Whiteboard whiteboard, Canvas canvas, String host, int port) {
        this.whiteboard = whiteboard;
        this.canvas = canvas;
        this.host = host;
        this.port = port;
    } //End Client constructor

    public void run() {
        try {
            //Obtain input from the server
            Socket toServer = new Socket(host, port);
            ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
            while(true) {
                //read from XML and pass the message object
                String xmlString = (String) in.readObject();
                XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
                MessageNotification message = (MessageNotification) decoder.readObject();
                //Read the message
                processMessage(message);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Client is responsible for processing the message sent by server
    public void processMessage(MessageNotification message)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ShapeController shape = canvas.getShapeID(message.getModel().getID());
                switch(message.getCommand()) {
                    case MessageNotification.ADD:
                        if(shape == null)
                            //Send a one-time populate to all clients based on requirement
                            canvas.addShape(message.getModel());
                        break;
                    case MessageNotification.UNDO:
                        if(shape != null)
                            canvas.clientUndoShapes(message.getModel(),shape);
                        break;
                    case MessageNotification.REDO:
                        if(shape != null)
                            canvas.clientRedoShapes(message.getModel(),shape);
                        break;
                    case MessageNotification.CHANGE:
                        if(shape != null)
                            shape.getModel().mimic(message.getModel());
                        break;
                    default:
                        break;
                }
            }
        });
    }
} //End Client Class
