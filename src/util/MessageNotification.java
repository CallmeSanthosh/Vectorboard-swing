
package util;

import model.ShapeModel;

public class MessageNotification {
    //Final "command" variables based on requirement
    public static final int ADD = 0;
    public static final int REMOVE = 1;
    public static final int UNDO = 2;
    public static final int REDO = 3;
    public static final int CHANGE = 4;
    public int command; //Command to be passed by server
    public ShapeModel model; //Model to be passed by server
    public MessageNotification() {
        command = -1;
        model = null;
    }
    public MessageNotification(int command, ShapeModel model) {
        this.command = command;
        this.model = model;
    } //End MessageNotification constructor

    public int getCommand() {
        return command;
    }

    public ShapeModel getModel() {
        return model;
    }

    public void setModel(ShapeModel model) {
        this.model = model;
    }

    public void setCommand(int command) {
        this.command = command;
    }
} //End MessageNotification
