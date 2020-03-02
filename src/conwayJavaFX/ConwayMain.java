package conwayJavaFX;

import conwayJavaFX.UserInterface;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/*******
* <p> Title: ConwayMain Class. </p>
* 
* <p> Description: A JavaFX demonstration application: This controller class defines the board
* so it fix on the computer's display, set up the user interface, and enables execution</p>
* 
* <p> Copyright: Lynn Robert Carter © 2018-05-06 </p>
* 
* @author Lynn Robert Carter
* 
* @version 2.03	2018-05-07 An implementation baseline for JavaFX graphics
* 
*/

public class ConwayMain extends Application {
	
	public UserInterface theGUI;
	
	public static double WINDOW_WIDTH;
	public static double WINDOW_HEIGHT;

	
	public void start(Stage theStage) throws Exception {
		// Determine the actual visual bounds for this display
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		//set Stage boundaries to the visual bounds so it does not total fill the screen 
		WINDOW_WIDTH = primaryScreenBounds.getWidth() - primaryScreenBounds.getMinX() - 100;
		WINDOW_HEIGHT = primaryScreenBounds.getHeight() - primaryScreenBounds.getMinY() - 100;
			
		theStage.setTitle("Conway's Game of Life");				// Label the stage (a window)
		
		Pane theRoot = new Pane();								// Create a pane within the window
		
		theGUI = new UserInterface(theRoot);					// Create the Graphical User Interface
		
		Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);	// Create the scene
		
		theStage.setScene(theScene);							// Set the scene on the stage
		
		theStage.show();										// Show the stage to the user
		
		// When the stage is shown to the user, the pane within the window is visible.  This means that the
		// labels, fields, and buttons of the Graphical User Interface (GUI) are visible and it is now 
		// possible for the user to select input fields and enter values into them, click on buttons, and 
		// read the labels, the results, and the error messages.
	}
	


	/*******************************************************************************************************/

	/*******************************************************************************************************
	 * This is the method that launches the JavaFX application
	 * 
	 */
	public static void main(String[] args) {					// This method may not be required
		launch(args);											// for all JavaFX applications using
	}															// other IDEs.

}
