package conwayJavaFXSpeedTest2;

public class RowProcessor implements Runnable {
	
	private boolean [][] previous;
	private boolean [][] next;
	private int row;
	
	public RowProcessor(boolean [][] b, boolean [][] n, int r) {
		previous = b;
		next = n;
		row = r;
	}
	
	public void run() {
		processRow(previous, next, row);
	}
	
	private void processRow(boolean [][] previous, boolean [][] next, int rowNumber) {
		boolean [] result = new boolean [1000];
				
		for (int y = 1; y < 1000-2; y++) {
			
			// Count the number of alive neighbor cells on the previous board
			int numberAliveCells = 0;
			if (previous[rowNumber-1][y-1]) numberAliveCells++;
			if (previous[rowNumber-1][y]) numberAliveCells++;
			if (previous[rowNumber-1][y+1]) numberAliveCells++;
			if (previous[rowNumber][y-1]) numberAliveCells++;
			if (previous[rowNumber][y+1]) numberAliveCells++;
			if (previous[rowNumber+1][y-1]) numberAliveCells++;
			if (previous[rowNumber+1][y]) numberAliveCells++;
			if (previous[rowNumber+1][y+1]) numberAliveCells++;
			
			// If the cell was alive, it must have 2 or 3 alive neighbors to remain alive
			if (previous[rowNumber][y]) { 
				if (numberAliveCells >= 2 && numberAliveCells <= 3)
					result[y] = true;
				
			// if the cell was not alive, it must have exactly 3 alive neighbors to give birth
			} else if (numberAliveCells == 3)
				result[y] = true;
		}
		next[rowNumber] = result;
	}

}
