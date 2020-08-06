package solver;

public class Solver {
	
	SudokuModel grid;
	private boolean[][] preGen;
	
	public Solver(SudokuModel grid, boolean[][] preGen) {
		this.grid = grid;
		this.preGen = preGen;
	}
	
	public boolean solve() {
		if (!preVerification()) {
			return false;
		}
		return solveHelper(0, 0);
	}
	
	private boolean solveHelper(int row, int col) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int nextRow = row;
		int nextCol = (col + 1) % 9;
		
		if (nextCol == 0) {
			nextRow = row + 1;
		}
		
		if (row == 9) {
			return true;
		}
		
		if (preGen[row][col]) {
			return solveHelper(nextRow, nextCol);
		}
		
		for (int value = 1; value < 10; value++) {

			if (isLegal(row, col, value)) {
				grid.put(row, col, value);
				//grid[row][col] = value;
				if (solveHelper(nextRow, nextCol)) {
					return true;
				} 
				grid.put(row, col, 0);
				//grid[row][col] = 0;
			}
		}
		
		return false;
	}
	
	public int[][] getSolution() {
		return grid.getGrid();
	}
	
	private boolean preVerification () {
		for (int i = 0; i < 9; i++) {
			for (int j = 0;j < 9; j++) {
				if (preGen[i][j]) {
					if (!isLegal(i, j, grid.get(i, j))) {
					//if (!isLegal(i, j, grid[i][j])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean isLegal(int row, int col, int value) {
		return 	checkBox(row, col, value) && 
				checkColumn(row, col, value) &&
				checkRow(row ,col, value);
	}
	
	private boolean checkBox(int row, int col, int value) {
		int boxRow = row / 3;
		int boxCol = col / 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (row == (i + boxRow*3) && col == (j + boxCol*3)) {
					continue;
				}
				if (grid.get(i + boxRow * 3, j + boxCol * 3) == value) {
				//if (grid[i+boxRow * 3][j+boxCol * 3] == value) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkColumn(int row, int col, int value) {
		for (int i = 0; i < 9; i++) {
			if (value == grid.get(i, col) && i != row) {
			//if (value == grid[i][col] && i != row) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRow(int row, int col, int value) {
		for (int i = 0; i < 9; i++) {
			if (value == grid.get(row, i) && i != col) {
			//if (value == grid[row][i] && i != col) {
				return false;
			}
		}
		return true;
	}
}
