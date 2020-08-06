package solver;

import java.util.Arrays;

public class SudokuModel {

	private Cell[][] grid = new Cell[9][9];
	boolean changed = false;
	
	public SudokuModel() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				grid[i][j] = new Cell();
			}
		}
	}

	public void put(int row, int col, int value) throws IndexOutOfBoundsException {
		grid[row][col].setValue(value);
	}
	
	public int get(int row, int col) {
		return grid[row][col].getValue();
	}
	
	public int[][] readGrid() throws InterruptedException {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				result[i][j] = grid[i][j].getValue();
			}
		}
		return result;
	}
	
	public int[][] getGrid() {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				result[i][j] = grid[i][j].getValue();
			}
		}
		return result;
	}
	
	public void subscribe(int row, int col, MyObserver o) {
		grid[row][col].subscribe(o);
	}
	
	private static class Cell implements MyObservable {
		private int value = 0;
		MyObserver observer;
		
		public void subscribe(MyObserver o) {
			this.observer = o;
		}
		
		public void setValue(int value) {
			this.value = value;
			observer.update(value);
		}
		
		public int getValue() {
			return value;
		}
		
	}
	
}
