package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import model.LineModel;
import model.EllipseModel;
import model.RectModel;
import model.ShapeModel;
import util.Client;
import util.Server;

public class Whiteboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel controlBox;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu backgroundMenu;
    private JLabel whiteboardLabel;
    private JLabel addLabel;
    private JLabel modeLabel;
    private JButton rectButton;
    private JButton ovalButton;
    private JButton lineButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton setButton;
    private JButton clientButton;
    private JButton serverButton;
    private JFileChooser fileChooser;
    private JTextField textField;
    private ArrayList<JComponent> allComponents;
    private Server serverAccepter;
    private Client clientHandler;
    private ArrayList<ObjectOutputStream> outputList = new ArrayList<>();
    private String serverMode = "Server Mode";
    private String clientMode = "Client Mode";
    private String basicMode = "Basic Mode";
    private String currentMode;
    private static int nextID = 0;
    private static int userPort;
    private Color bgCustomColor = Color.decode("#04839a");
    private Canvas canvas;

    public Whiteboard() {
        this.setTitle("Vector board");
        setLayout(new BorderLayout());
        //Box component where all controls/buttons are
        controlBox = new JPanel();
        controlBox.setBackground(bgCustomColor);
        controlBox.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "CS 5001 - Santhosh Kumar Jayavel"));
        controlBox.setLayout(new GridLayout(6,0));
        allComponents = new ArrayList<>();
        createMenuBar();
        createWhiteboardLabelBox();
        createButtonBox();
        createOperationButtonBox();
        createOperationButton();
        //Add control box to whiteboard
        add(controlBox, BorderLayout.WEST);
        //Set new canvas and add to whiteboard
        canvas = new Canvas(this);
        canvas.setBorder(new LineBorder(Color.black,2));
        add(canvas, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    } //End Whiteboard Constructor

    //Add shape to the Canvas
    private void addShape(ShapeModel model) {
        if(model instanceof LineModel) {
            //Cast as a DLine
            ((LineModel) model).modifyWithPoints(new Point(Canvas.INITIAL_POSITION, Canvas.INITIAL_POSITION)
                    , new Point(Canvas.INITIAL_POSITION + Canvas.INITIAL_SIZE, Canvas.INITIAL_POSITION + Canvas.INITIAL_SIZE));
        }
        else {
            //Changed to an initial final size and position based on requirement
            model.setBounds(Canvas.INITIAL_POSITION, Canvas.INITIAL_POSITION, Canvas.INITIAL_SIZE, Canvas.INITIAL_SIZE);
        }
        canvas.addShape(model);
    } //End addShape

    private void createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        backgroundMenu = new JMenu("Background");
        JMenuItem save = new JMenuItem("Save Canvas");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCanvas();
            }
        });

        JMenuItem open = new JMenuItem("Open Existing Canvas");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCanvas();
            }
        });

        JMenuItem clear = new JMenuItem("Clear Canvas");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.clear();
            }
        });

        JMenuItem image = new JMenuItem("Save to PNG");
        image.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCanvasImage();
            }
        });

        JMenuItem createWindow = new JMenuItem("Open New Window");
        createWindow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewWindow();
            }
        });
        createControlBackgroundMenu();
        fileMenu.add(save);
        fileMenu.add(open);
        fileMenu.add(clear);
        fileMenu.add(image);
        fileMenu.addSeparator();
        fileMenu.add(createWindow);
        menuBar.add(fileMenu);
        menuBar.add(backgroundMenu);
        allComponents.add(createWindow);
        allComponents.add(open);
        allComponents.add(clear);
        setJMenuBar(menuBar);
    } //End createMenuBar

    private void createWhiteboardLabelBox() {
        Box panel = Box.createVerticalBox();
        panel.setBorder(new LineBorder(Color.black,2));
        panel.setPreferredSize(new Dimension(100,100));
        whiteboardLabel = new JLabel("<html><center>Vector board</center>Control Panel</html>", SwingConstants.CENTER);
        whiteboardLabel.setFont(new Font("Serif", Font.BOLD, 26));
        whiteboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel = new JLabel(basicMode);
        currentMode = basicMode;
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(whiteboardLabel);
        panel.add(modeLabel);
        controlBox.add(panel);
    } //End createWhiteboardLabelBox

    //Creates a box where the add shapes buttons are
    private void createButtonBox() {
        //Create box for shape panel
        Box panel = Box.createHorizontalBox();
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        addLabel = new JLabel("Add Shapes: ");
        //Rectangle button
        rectButton = new JButton("Rect");
        rectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addShape(new RectModel());
            }
        });
        //Oval button
        ovalButton = new JButton("Oval");
        ovalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addShape(new EllipseModel());
            }
        });
        //Line button
        lineButton = new JButton("Line");
        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addShape(new LineModel());
            }
        });
        panel.add(addLabel);
        panel.add(rectButton);
        panel.add(ovalButton);
        panel.add(lineButton);
        allComponents.add(addLabel);
        allComponents.add(rectButton);
        allComponents.add(ovalButton);
        allComponents.add(lineButton);
        fileChooser = new JFileChooser();
        //Add button box panel to overall control box
        controlBox.add(panel, BorderLayout.WEST);
    } //End createButtonBox

    private void createOperationButton() {
        Box panel = Box.createHorizontalBox();
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        this.addLabel = new JLabel("Networking: ");
        serverButton = new JButton("Server");
        serverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Prevents the user from initiating a new server
                if(Whiteboard.this.runningAsServer()) {
                    JOptionPane.showMessageDialog(Whiteboard.this, "Current window is already running as a Server.", "Server Running", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    serverSide();
                }
            }
        });
        clientButton = new JButton("Client");
        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Double checks to see if current window isn't in server mode already
                //This prevents the window from going to Server to Client
                if(Whiteboard.this.runningAsServer()) {
                    JOptionPane.showMessageDialog(Whiteboard.this, "Current window is already running as a Server.", "Server Running", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    clientSide();
                }
            }
        });
        panel.add(addLabel);
        panel.add(serverButton);
        panel.add(clientButton);
        allComponents.add(addLabel);
        allComponents.add(serverButton);
        allComponents.add(clientButton);
        for(Component comp : panel.getComponents())
            ((JComponent) comp).setAlignmentX(Box.LEFT_ALIGNMENT);

        controlBox.add(panel, BorderLayout.WEST);
    } //End createOperationButton

    private void createOperationButtonBox() {
        Box panel = Box.createHorizontalBox();
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        this.addLabel = new JLabel("Action: ");
        this.redoButton = new JButton("Redo");
        this.redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                canvas.redoShapes();
            }
        });
        this.undoButton = new JButton("Undo");
        this.undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                canvas.undoShapes();
            }
        });
        this.setButton = new JButton("Set Color");
        this.setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = null;
                color = JColorChooser.showDialog(null, "Choose Color", color);
                canvas.setColor(color);
            }
        });
        panel.add(addLabel);
        panel.add(redoButton);
        panel.add(undoButton);
        panel.add(setButton);
        allComponents.add(addLabel);
        allComponents.add(redoButton);
        allComponents.add(undoButton);
        allComponents.add(setButton);
        for(Component comp : panel.getComponents())
            ((JComponent) comp).setAlignmentX(Box.LEFT_ALIGNMENT);
        controlBox.add(panel, BorderLayout.WEST);
    } //End createOperationButtonBox

    private void createControlBackgroundMenu() {
        JMenuItem bgGreen = new JMenuItem("Green");
        bgGreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Color green = Color.decode("#017b1b");
                controlBox.setBackground(green);
            }
        });
        JMenuItem bgRed = new JMenuItem("Red");
        bgRed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color red = Color.decode("#980606");
                controlBox.setBackground(red);
            }
        });
        JMenuItem bgBlue = new JMenuItem("Blue");
        bgBlue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color blue = Color.decode("#0a1bbf");
                controlBox.setBackground(blue);
            }
        });
        JMenuItem bgWhite = new JMenuItem("White");
        bgWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controlBox.setBackground(new Color(238,238,238));
            }
        });
        JMenuItem bgDefault = new JMenuItem("Default");
        bgDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                controlBox.setBackground(bgCustomColor);
            }
        });
        backgroundMenu.add(bgRed);
        backgroundMenu.add(bgGreen);
        backgroundMenu.add(bgBlue);
        backgroundMenu.add(bgWhite);
        backgroundMenu.addSeparator();
        backgroundMenu.add(bgDefault);
    } //End createControlBackgroundMenu

    private void saveCanvas() {
        int value = fileChooser.showSaveDialog(this);
        if(value == JFileChooser.APPROVE_OPTION) {
            canvas.save(fileChooser.getSelectedFile()); //Get current file
        }
    } //End saveCanvas

    private void openCanvas() {
        int value = fileChooser.showOpenDialog(this);
        if(value == JFileChooser.APPROVE_OPTION) {
            canvas.open(fileChooser.getSelectedFile());
        }
    } //End openCanvas

    private void saveCanvasImage() {
        int value = fileChooser.showSaveDialog(this);
        if(value == JFileChooser.APPROVE_OPTION) {
            canvas.saveImageFile(fileChooser.getSelectedFile());
        }
    }

    public void serverSide() {
        String currentPort = JOptionPane.showInputDialog("Server Port: ", "39587");
        if(currentPort != null) {
            userPort = Integer.parseInt(currentPort); //User specified port
            Color color = Color.decode("#7c0320");
            currentMode = serverMode;
            modeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
            modeLabel.setText(serverMode);
            modeLabel.setForeground(color);
            controlBox.setBackground(new Color(238,238,238));
            serverAccepter = new Server(this, canvas, userPort);
            serverAccepter.start();
        }
    } //End serverSide

    public void clientSide() {
        String connectingPort = JOptionPane.showInputDialog("Connect to server host:port: ", "127.0.0.1:" + userPort);
        if(connectingPort != null) {
            String[] portNumber = connectingPort.split(":");
            //Disable controls for Client side
            for(JComponent components: allComponents) {
                components.setEnabled(false);
            }
            currentMode = clientMode;
            modeLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
            modeLabel.setText(clientMode);
            modeLabel.setForeground(Color.decode("#7f6000"));
            clientHandler = new Client(this, canvas, portNumber[0].trim(), Integer.parseInt(portNumber[1]));
            clientHandler.start();
        }
    } //End clientSide

    public boolean runningAsServer() {
        return currentMode.equals(serverMode);
    } //End runningAsServer

    public boolean runningAsClient() {
        return currentMode.equals(clientMode);
    } //End runningAsClinet

    public JTextField getTextField() {
        return textField;
    }

    public ArrayList<ObjectOutputStream> getOutputs() {
        return outputList;
    }

    public static int getNextID() {
        return nextID++;
    }

    public Server getServer() {
        return serverAccepter;
    }

    public Client getClient() {
        return clientHandler;
    }

    private void createNewWindow() {
        Whiteboard whiteboard = new Whiteboard();
    }

    public static void main(String[] args) {
        Whiteboard whiteboard = new Whiteboard();
    }
} //End Whiteboard class
