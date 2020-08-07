package solver;

public class Solver {

	SudokuModel grid;
	private boolean[][] preGen;
	private int speed;

	public Solver(SudokuModel grid, boolean[][] preGen) {
		this(grid, preGen, 0);
	}

	public Solver(SudokuModel grid, boolean[][] preGen, int speed) {
		this.grid = grid;
		this.preGen = preGen;
		this.speed = speed;
	}

	public boolean solve() {
		return solveHelper(0, 0);
	}

	private boolean solveHelper(int row, int col) {
		sleep();

		if (row == 9) {
			return true;
		}

		int nextCol = (col + 1) % 9;
		int nextRow = nextCol == 0 ? row + 1 : row;

		if (preGen[row][col]) {
			return solveHelper(nextRow, nextCol);
		}

		for (int value = 1; value < 10; value++) {
			if (grid.isLegal(row, col, value)) {
				
				grid.put(row, col, value);
				//sleep();
				if (solveHelper(nextRow, nextCol)) {
					return true;
				}
				grid.remove(row, col);
			}
		}

		return false;
	}

	public int[][] getSolution() {
		return grid.readGrid();
	}

	private void sleep() {
		try {
			Thread.sleep(speed);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
