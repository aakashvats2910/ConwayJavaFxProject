package conwayJavaFX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

/*******
 * <p> Title: UserInterface Class. </p>
 * 
 * <p> Description: A JavaFX demonstration application: This controller class describes the user
 * interface for the Conway's Game of Life </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2018-05-06 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.03	2018-05-07 An implementation baseline for JavaFX graphics
 * 
 */
public class UserInterface {
	
	/**********************************************************************************************

	Class Attributes
	
	**********************************************************************************************/

	// Attributes used to establish the board and control panel within the window provided to us
	private double controlPanelHeight = ConwayMain.WINDOW_HEIGHT - 110;
	private double windowSizeWidth = ConwayMain.WINDOW_WIDTH - 40;
	private double windowSizeHeight = controlPanelHeight-6;
	private int cellSize = 6;
	private int boardSizeWidth = (int)windowSizeWidth/cellSize;
	private int boardSizeHeight = (int)(windowSizeHeight)/cellSize;
	private int marginWidth = 20;

	// The User Interface widgets used to control the user interface and start and stop the simulation
	private Label label_FileName = new Label("Enter the name of the game's file here:");
	private TextField text_FileName = new TextField();
	private Button button_Load = new Button("Load the pattern");
	private Button button_Start = new Button("Start");
	private Button button_Stop = new Button("Stop");

	// The attributes used to specify and assess the validity of the data file that defines the game
	private String str_FileName;			// The string that the user enters for the file name
	private Scanner scanner_Input = null;	// The Scanners used to evaluate whether or not the
	private Scanner scanner_Line = null;	// the specified file holds usable data

	// The attributes used to inform the user if the file name specified exists or not
	private Label message_FileFound = new Label("");
	private Label message_FileNotFound = new Label("");

	// These attributes are used to tell the user, in detail, about errors in the input data
	private String errorMessage_FileContents = "";
	private Label message_ErrorDetails = new Label("");

	// These attributes put a graphical frame around the portion of the window that receives the
	// black squares representing alive cells
	private Rectangle rect_outer =  new Rectangle(0,0,ConwayMain.WINDOW_WIDTH, controlPanelHeight-5);
	private Rectangle rect_middle = new Rectangle(5,5,ConwayMain.WINDOW_WIDTH-10, controlPanelHeight-15);
	private Rectangle rect_inner =  new Rectangle(6,6,ConwayMain.WINDOW_WIDTH-12, controlPanelHeight-17);

	// This attribute retains a copy of a reference to the application's Pane 
	private Pane window;

	// These attributes define the Board used by the simulation and the graphical representation
	// There are two Boards. The previous Board and the new Board.  Once the new Board has been
	// displayed, it becomes the previous Board for the generation of the next new Board.
	private NewClass oddGameBoard = new NewClass();		// The Board for odd frames of the animation
	private Pane oddCanvas = new Pane();			// Pane that holds its graphical representation
	
	private NewClass evenGameBoard =  new NewClass();		// The Board for even frames of the animation
	private Pane evenCanvas = new Pane();			// Pane that holds its graphical representation

	private boolean toggle = true;					// A two-state attribute that specifies which
													// is the previous Board and which is the new
	
	/**********************************************************************************************

	Constructors
	
	**********************************************************************************************/

	/**********
	 * This constructor established the user interface with all of the graphical widgets that are
	 * use to make the user interface work.
	 * 
	 * @param theRoot	This parameter is the Pane that JavaFX expects the application to use when
	 * 					it sets up the GUI elements.
	 */
	public UserInterface(Pane theRoot) {
		
		// Establish the attribute that the rest of the system can use to manipulate the GUI
		window = theRoot;
		
		// Set the fill colors for the border frame for the game's output of the simulation
		rect_outer.setFill(Color.LIGHTGRAY);
		rect_middle.setFill(Color.BLACK);
		rect_inner.setFill(Color.WHITE);

		// Label the text field that is to receive the file name.
		setupLabelUI(label_FileName, "Arial", 18, ConwayMain.WINDOW_WIDTH-20, Pos.BASELINE_LEFT, 
				marginWidth, controlPanelHeight);

		// Establish the text input widget so the user can enter in the name of the file that holds
		// the data about which cells are alive at the start of the simulation
		setupTextUI(text_FileName, "Arial", 18, ConwayMain.WINDOW_WIDTH / 2, Pos.BASELINE_LEFT, 
				marginWidth, controlPanelHeight + 24, true);
		
		// Establish the link between the text input widget and a routine that checks to see if
		// if a file of that name exists and if so, whether or not the data is valid
		text_FileName.textProperty().addListener((observable, oldValue, newValue) -> {checkFileName(); });

		// Establish a GUI button the user presses when the file name have been entered and the
		// code has verified that the data in the file is valid.
		setupButtonUI(button_Load, "Arial", 18, 100, Pos.BASELINE_LEFT, ConwayMain.WINDOW_WIDTH - 275,  
				controlPanelHeight + 24);
		
		// Establish the link between the button widget and a routine that loads the data into a
		// Board and displays the data to the user.
		button_Load.setOnAction((event) -> { loadImageData(); });

		// Establish a GUI button that the user presses to start the simulation
		setupButtonUI(button_Start, "Arial", 18, 50, Pos.BASELINE_LEFT, ConwayMain.WINDOW_WIDTH - 80,  
				controlPanelHeight + 24);
		
		// Link the start button to the routine that sets up the simulation to run and starts it
		button_Start.setOnAction((event) -> { startConway(); });

		// Establish a GUI button that the user can press once the simulation starts to stop it
		setupButtonUI(button_Stop, "Arial", 18, 50, Pos.BASELINE_LEFT, ConwayMain.WINDOW_WIDTH - 80,  
				controlPanelHeight + 24);
		
		// Link the stop button to the routine that stops the simulation and terminates the program
		// execution
		button_Stop.setOnAction((event) -> { stopConway(); });

		// Disable the buttons (They will appear grayed out)
		button_Load.setDisable(true);
		button_Start.setDisable(true);

		// The following set up the control panel messages for messages and information about errors
		setupLabelUI(message_FileFound, "Arial", 18, 150, Pos.BASELINE_LEFT, 350, controlPanelHeight);
		message_FileFound.setStyle("-fx-text-fill: green; -fx-font-size: 18;");

		setupLabelUI(message_FileNotFound, "Arial", 18, 150, Pos.BASELINE_LEFT, 350, controlPanelHeight);
		message_FileNotFound.setStyle("-fx-text-fill: red; -fx-font-size: 18;");

		setupLabelUI(message_ErrorDetails, "Arial", 16, ConwayMain.WINDOW_WIDTH, Pos.BASELINE_LEFT, 20, 
				ConwayMain.WINDOW_HEIGHT-48);
		message_ErrorDetails.setStyle("-fx-text-fill: red; -fx-font-size: 16;");

		
		// Place all of the just-initialized GUI elements into the pane with the exception of the
		// Stop button.  That widget will replace the Start button, once the Start has been pressed
		theRoot.getChildren().addAll(rect_outer, rect_middle, rect_inner, label_FileName, text_FileName, 
				button_Load, button_Start, message_FileFound, message_FileNotFound, message_ErrorDetails);
	}

	
	/**********************************************************************************************

	Helper methods - Used to set up the JavaFX widgets and simplify the code above
	
	**********************************************************************************************/

	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}

	/**********
	 * Private local method to initialize the standard fields for a button
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	/**********************************************************************************************

	Action methods - Used cause things to happen with the set up or during the simulation
	
	**********************************************************************************************/

	/**********
	 * This routine checks, after each character is typed, to see if the game of life file is there
	 * and if so, sets up a scanner to it and enables a button to read it and run the simulation.  
	 * If a file is not found, a warning message is displayed and the button is disabled.
	 */
	void checkFileName(){
		str_FileName = text_FileName.getText();			// Whenever the text area for the file name is changed
		if (str_FileName.length()<=0){					// this routine is called to see if it is a valid filename.
			message_FileFound.setText("");				// Reset the messages to empty
			message_FileNotFound.setText("");
			scanner_Input = null;
		} else 											// If there is something in the file name text area
			try {										// this routine tries to open it and establish a scanner.
				scanner_Input = new Scanner(new File(str_FileName));

				// There is a readable file there... this code checks the data to see if it is valid 
				// for this application (User input errors are GUI issues, not simulation issues.)
				if (fileContentsAreValid()) {
					message_FileFound.setText("File found and the contents are valid!");
					message_ErrorDetails.setText("");
					message_FileNotFound.setText("");
					button_Load.setDisable(false);		// Enable the Start button
					button_Start.setDisable(true);					
				}
				
				// If the methods returns false, it means there is a problem with input file
				else {	// and the method has set up a String to explain what the issue is
					message_FileFound.setText("");
					message_FileNotFound.setText("File found, but the contents are not valid!");
					message_ErrorDetails.setText(errorMessage_FileContents);
					button_Load.setDisable(true);		// Keep the buttons disabled
					button_Start.setDisable(true);
				}

			} catch (FileNotFoundException e) {			// If an exception is thrown, the file name
				message_FileFound.setText("");			// that the button to run the simulation is
				message_FileNotFound.setText("File not found!");	// not enabled.
				message_ErrorDetails.setText("");
				scanner_Input = null;								
				button_Load.setDisable(true);			// Keep the buttons disabled
				button_Start.setDisable(true);
			}
	}

	/**********
	 * This method is called when the Load button is pressed. It tries to load the data onto the
	 * odd Board and sets up the even board for the simulation.
	 */
	private void loadImageData() {
		try {
			oddGameBoard = new NewClass(boardSizeWidth, boardSizeHeight, new Scanner(new File(str_FileName)));
			evenGameBoard = new NewClass(boardSizeWidth, boardSizeHeight);
		}
		catch (FileNotFoundException e)  {
			// Since we have already done this check, this exception should never happen
		}
		oddGameBoard.populateCanvas(oddCanvas);		// Given the odd data, populate the odd canvas
		window.getChildren().add(oddCanvas);		// Add the odd canvas to the display so the
													// user can see if this is the right data
		
		button_Load.setDisable(true);				// Disable the Load button, since it is done
		button_Start.setDisable(false);				// Enable the Start button
	};												// and wait for the User to press it.

	/**********
	 * This method removes the start button, sets up the stop button, and starts the simulation
	 */
	private void startConway() {
		window.getChildren().remove(button_Start);	// Remove the start button
		window.getChildren().add(button_Stop);		// Add in the stop button
		
		// Start the simulation by means of an animation Timeline using a keyframe duration of 
		// 50 milliseconds
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), ae -> runSimulation()));
		timeline.setCycleCount(Animation.INDEFINITE);	// The animation runs until it is stopped
		timeline.play();								// Start the animation
	};
	
	/**********
	 * This method display the current state of the odd board and terminates the application
	 */
	private void stopConway() {
		System.out.println(oddGameBoard);
		System.exit(0);
	}

	/**********
	 * This method is run each time the timeline triggers it
	 */
	public void runSimulation(){
		// Use the toggle to flip back and forth between the even and the odd boards
		if (toggle) {
			
			// When the toggle is true, we use the odd board as the previous board and that data
			evenGameBoard.step(oddGameBoard);			// sets up the new board (even)

			window.getChildren().remove(oddCanvas);		// Discard the old (oddCanvas) data
			
			evenCanvas = new Pane();					// Set up a new empty even canvas
			
			evenGameBoard.populateCanvas(evenCanvas);	// Populate the even canvas using the
														// data from the even board
			
			window.getChildren().add(evenCanvas);		// Add the even canvas to the window so
														
			
			toggle = false;								// set up so next call uses the even
		}												// board as the previous
		else {
			
			// When the toggle is false, we use the even board as the previous board and that data
			oddGameBoard.step(evenGameBoard);			// sets up the new board (odd)
			
			window.getChildren().remove(evenCanvas);	// Discard the old (evenCanvas) data
			
			oddCanvas = new Pane();						// Set up a new empty odd canvas
			
			oddGameBoard.populateCanvas(oddCanvas);		// Populate the odd canvas using the
														// data from the even board
			
			window.getChildren().add(oddCanvas);		// Add the even canvas to the window so
														// that frame can be seen
			
			toggle = true;								// set up so next call uses the odd
		}												// board as the previous
	}

	/**********
	 * This method reads in the contents of the data file and discards it as quickly as it reads it
	 * in order to verify that the data meets the input data specifications and helps reduce the 
	 * change that invalid input data can lead to some kind of hacking.
	 * 
	 * @return	true - 	when the input file *is* valid
	 * 					when the input file data is *not* valid - The method also sets a string with
	 * 						details about what is wrong with the input data so the user can fix it
	 */
	private boolean fileContentsAreValid() {
		
		// Declare and initialize data variables used to control the method
		int firstValue = -1;
		int secondValue = -1;
		int lineNumber = 0;
		
		// Process each and every line in the input file
		while (scanner_Input.hasNextLine()) {
			lineNumber++;								// Keep track of which line is being used
			
			// Read in the line and trim the white space before and after the real data
			String inputLine = scanner_Input.nextLine().trim();
			
			// Construct a scanner to parse each input line (should be two integer values/line)
			scanner_Line = new Scanner(inputLine);
			
			// See if the first thing on the line is an int
			if (scanner_Line.hasNextInt()) {
				firstValue = scanner_Line.nextInt();
				
				// If so, make sure the value fits onto the board
				if (firstValue < 0 || firstValue >= boardSizeHeight) {
					errorMessage_FileContents = "First value is out of range (0 - " + (boardSizeHeight-1) + ").\n" +
							"Line number " + lineNumber + ": " + inputLine;	
					return false;						// First value (row index) is out of range
				}
				
				// See if the second thing is another int
				if (scanner_Line.hasNextInt()) {
					secondValue = scanner_Line.nextInt();
					
					// If so, make sure the value fits onto the board
					if (secondValue < 0 || secondValue >= boardSizeWidth) {
						errorMessage_FileContents = "Second value is out of range (0 - " + (boardSizeWidth-1) + ").\n" +
								"Line number " + lineNumber + ": " + inputLine;	
						return false;			// Second value (col index) is out of range
					}
					
					// After two int values on the line, there should be nothing else
					if (scanner_Line.hasNext()) {
						errorMessage_FileContents = "It is not valid to have characters following the two integers on a line.\n" +
								"Line number " + lineNumber + ": " + inputLine;	
						return false;			// Additional characters after two integers values
					}
				}
				else  {
					
					// If the execution comes here the second item is not an int
					errorMessage_FileContents = "The second item on a line is not an integer value.\n" +
							"Line number " + lineNumber + ": " + inputLine;	
					return false;				// Second item is not a long
				}
			}
			else {
				
				// If the execution comes here the first item is not an int
				errorMessage_FileContents = "The first item on a line is not an integer value.\n" +
						"Line number " + lineNumber + ": " + inputLine;	
				return false;					// First item is not a long
			}
		}
		
		// Should the execution reach here, the input file appears to be valid
		errorMessage_FileContents = "";
		return true;							// End of file found 
	}
}
