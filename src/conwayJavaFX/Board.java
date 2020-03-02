package conwayJavaFX;

import java.util.Scanner;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*******
 * <p> Title: Board Class. </p>
 * 
 * <p> Description: A JavaFX demonstration application: This entity class describes the board that
 * enables Conway's Game of Life </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2018 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.00	2018-04-27 An implementation baseline for JavaFX graphics
 * 
 */
public class Board {
	
	/**********************************************************************************************

	Class Attributes
	
	**********************************************************************************************/
	
	// These are the major attribute values for this application
	protected boolean cellIsAlive [][];
	
	/**********************************************************************************************

	Constructors
	
	**********************************************************************************************/
	
	/**********
	 * This is the default constructor.  We do not expect it to be used.
	 */
	public Board () {
		cellIsAlive = new boolean[3][3];				// Initialized to false by default;
	}
	
	/**********
	 * This constructor creates a board of a specific size. This constructor is used to produce 
	 * instances of a board for the purposes of animation.
	 */
	public Board (int height, int width) {
		cellIsAlive = new boolean[height][width];		// Initialized to false by default;
	}
	
	/**********
	 * This constructor creates a board of a specific size and the initializes if based on input
	 * from a specified scanner. This method assumes that the format of the data in the file has
	 * already been checked by the user interface and any errors in the format would have been
	 * dealt with by that code.
	 */
	public Board (int height, int width, Scanner inputReader) {
		cellIsAlive = new boolean[height][width];		// Initialized to false by default;
		
		// The input is a sequence of lines, two integer values per line. Each pair is the x
		// and the y coordinate where a living cell should be placed.
		while (inputReader.hasNextLine()) {
			String inputLine = inputReader.nextLine();	// Keep reading lines until they are gone
			Scanner input = new Scanner(inputLine);		// Set up a scanner for each line
			int x = input.nextInt();					// Extract the pair of coordinates
			int y = input.nextInt();
			cellIsAlive[x][y] = true;					// Signal that that cell is alive
			input.close();								// Close the Scanner
		}
	}
	
	/**********************************************************************************************

	Standard support methods
	
	**********************************************************************************************/
	
	/**********
	 * This is the debugging toString method, written to support boards up through 999 rows.
	 */
	public String toString() {
		String result = "";								// Insert the right number of zeros so the
		for (int y = 1; y < cellIsAlive[0].length-1; y++) {		// element in the board align with
			if (y<10) result += "[00" + y + "] ";				// one another
			else if (y<100) result += "[0" + y + "] ";
			else result += "[" + y + "] ";
			for (int x = 1; x < cellIsAlive.length-1; x++)
				if (cellIsAlive[x][y]) result += "*";	// Display an asterisk if the cell is alive
				else result += " ";						// else display a blank
			result += "\n";								// Append an end of line for the next row
		}
		return result;									// Return the string to the caller
	}
	
	
}