package conwayJavaFX;

import java.util.Scanner;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NewClass extends Board{
	
	NewClass() {
		super();
	}
	
	NewClass(int height, int width) {
		super(height,  width);
	}
	
	NewClass(int height, int width, Scanner inputReader) {
		super(height, width, inputReader);
	}
	//mulla pakistani
	/**********************************************************************************************

	Board oriented methods
	
	**********************************************************************************************/
	
	/**********
	 * This method places a number of black squares into a specified window pane, one for each live
	 * cell on *this* board.
	 */
	public void populateCanvas(Pane p) {
		for (int x = 1; x < cellIsAlive.length-1; x++)
			for (int y = 1; y < cellIsAlive[0].length-1; y++)
				if (cellIsAlive[x][y]) {
					Rectangle rectangle = new Rectangle(5,5,Color.BLACK);
				    rectangle.relocate(6*x, 6*y);
				    p.getChildren().add(rectangle);
				}
	}
	
	/**********
	 * This method determines which cells are alive for *this* board based on which cells were 
	 * alive on the previous board.
	 * 
	 * The method creates a new board (each element defaults to false) and then examines each cell
	 * in the previous board that is not on the boundary to see how many of the 8 potential cells 
	 * are currently alive.  If the examined cell *was* alive, then it must have at least two but
	 * no more than three alive neighbors to stay alive.  If the examined cell *was not* alive on
	 * the previous board, it must have exact three alive neighbors on the previous board for the
	 * cell to be alive on *this* board.
	 */
	public void step (Board previous) {
		
		// Allocate a new copy of this board so all elements are false
		cellIsAlive = new boolean[cellIsAlive.length][cellIsAlive[0].length];
		
		// Iterate through all of the elements on the previous boards, not on the boundary
		for (int x = 1; x < cellIsAlive.length-1; x++)
			for (int y = 1; y < cellIsAlive[0].length-1; y++) {
				
				// Count the number of alive neighbor cells on the previous board
				int numberAliveCells = 0;
				if (previous.cellIsAlive[x-1][y-1]) numberAliveCells++;
				if (previous.cellIsAlive[x-1][y]) numberAliveCells++;
				if (previous.cellIsAlive[x-1][y+1]) numberAliveCells++;
				if (previous.cellIsAlive[x][y-1]) numberAliveCells++;
				if (previous.cellIsAlive[x][y+1]) numberAliveCells++;
				if (previous.cellIsAlive[x+1][y-1]) numberAliveCells++;
				if (previous.cellIsAlive[x+1][y]) numberAliveCells++;
				if (previous.cellIsAlive[x+1][y+1]) numberAliveCells++;
				
				// If the cell was alive, it must have 2 or 3 alive neighbors to remain alive
				if (previous.cellIsAlive[x][y]) { 
					if (numberAliveCells >= 2 && numberAliveCells <= 3)
						cellIsAlive[x][y] = true;
					
				// if the cell was not alive, it must have exactly 3 alive neighbors to give birth
				} else if (numberAliveCells == 3)
					cellIsAlive[x][y] = true;
			}
	}

}
