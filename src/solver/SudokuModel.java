package solver;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class SudokuModel {

	private Cell[][] grid = new Cell[9][9];
	boolean changed = false;
	Map<Integer, Set<Integer>> boxes = new HashMap<Integer, Set<Integer>>();
	Map<Integer, Set<Integer>> rows = new HashMap<Integer, Set<Integer>>();
	Map<Integer, Set<Integer>> cols = new HashMap<Integer, Set<Integer>>();
	
	public SudokuModel() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				grid[i][j] = new Cell();
			}
		}
		reset();
	}

	public void put(int row, int col, int value) {
		grid[row][col].setValue(value);
		rows.get(row).add(value);
		cols.get(col).add(value);
		boxes.get(boxIndex(row, col)).add(value);
	}
	
	public int get(int row, int col) {
		return grid[row][col].getValue();
	}
	
	public int remove(int row, int col) {
		int prev = grid[row][col].getValue();
		grid[row][col].setValue(0);
		
		rows.get(row).remove(prev);
		cols.get(col).remove(prev);
		boxes.get(boxIndex(row, col)).remove(prev);
		
		return prev;
	}
	
	public int[][] readGrid() {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				result[i][j] = grid[i][j].getValue();
			}
		}
		return result;
	}
	
	public void reset() {
		for (int i = 0; i < 9; i++) {
			boxes.put(i, new HashSet<Integer>());
			rows.put(i, new HashSet<Integer>());
			cols.put(i, new HashSet<Integer>());
		}
	}
	
	public void subscribe(int row, int col, MyObserver o) {
		grid[row][col].subscribe(o);
	}
	
	public boolean isLegal(int row, int col, int value) {
		return 	!boxes.get(boxIndex(row, col)).contains(value) && 
				!cols.get(col).contains(value) &&
				!rows.get(row).contains(value);
	}
	
	private int boxIndex(int row, int col) {
		return (row / 3) * 3 + col / 3;
	}
	
	private static class Cell implements MyObservable {
		private int value = 0;
		MyObserver observer;
		
		public void subscribe(MyObserver o) {
			this.observer = o;
		}
		
		public void setValue(int value) {
			this.value = value;
			if (observer != null) {
				observer.update(value);
			}
		}
		
		public int getValue() {
			return value;
		}	
	}
}
