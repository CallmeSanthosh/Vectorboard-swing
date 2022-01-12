# Vectorboard using Java Swing

Design a simple vector graphics drawing program using Java Swing. This projec used the Model–View–Controller (MVC) design pattern.

## Features Implemented
* We can draw various shapes after clicking buttons- Line, Rectangle, Ellipse
* By default, small shape is drawn after clicking the respective shape button 
* After appearing on the screen it can be resized and repositioned across the board
* Contains colour chooser to pick a colour of the shapes
* Implemented undo and redo button left window
* Implemented Networking - click server to initiate server socket and from file menu create new tab and then click client button. Now the user doing in server is visible in client tab.
* Implemented Load and save vector drawing. From file menu one can save and upload existing vector templates.

To run the program - run Whiteboard class in view folder

## JUnit tests
To run the test suite

### compile

javac -cp .\src\junit.jar;.\src\hamcrest.jar;.\src\;. src\tests\*.java

### Run

java -cp .\src\junit.jar:.\src\hamcrest.jar:.\src\:. org.junit.runner.JUnitCore tests.ModelTestSuite
