package solver;

import java.util.Arrays;

public class SudokuModel {

	private int[][] grid = new int[9][9];
	boolean changed = false;

	public synchronized void put(int row, int col, int value) throws IndexOutOfBoundsException {
		grid[row][col] = value;
		changed = true;
		notifyAll();
	}
	
	public synchronized int get(int row, int col) {
		return grid[row][col];
	}
	
	public synchronized int[][] readGrid() throws InterruptedException {
		while (!changed) {
			wait();
		}
		changed = false;
		return Arrays.copyOf(grid, grid.length);
//		return grid;
	}
	
	public synchronized int[][] getGrid() {
		return Arrays.copyOf(grid, grid.length);
	}
	
}
