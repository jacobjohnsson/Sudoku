package controller;

import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import solver.Solver;
import solver.SudokuModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class SudokuController {
	
	private JTextField[][] textGrid;
	
	public SudokuController() {
		SwingUtilities.invokeLater(() -> createWindow());
	}
	
	private void createWindow() {
		textGrid = new JTextField[9][9];
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = frame.getContentPane();
		
		JButton clearButton = new JButton("Clear");
		JButton solveButton = new JButton("Solve");	
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(clearButton);
		buttonPanel.add(solveButton);
		pane.add(buttonPanel, BorderLayout.PAGE_END);
		
		clearButton.addActionListener(event -> clear());
		solveButton.addActionListener(event -> new Thread (() -> solve()).start());
		
		JPanel grid = new JPanel(new GridLayout(9,9,5,5));
		
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				JTextField textField = new JLimitedTextField(1);
				textField.setPreferredSize(new Dimension(30, 30));
				textField.setBackground(getColor(row, col));
				textGrid[row][col] = textField;
				grid.add(textField);
			}
		}
		
		pane.add(grid);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void clear() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				textGrid[row][col].setText("");
			}
		}
	}
	
	
	private void solve() {
		boolean[][] preGen = new boolean[9][9];
		SudokuModel matrix = new SudokuModel();
		
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (textGrid[row][col].getText().equals("")) {
					matrix.put(row, col, 0);
				} else {
					matrix.put(row, col, Integer.parseInt(textGrid[row][col].getText()));
					preGen[row][col] = true;
				}
			}
		}
		
		Solver solver = new Solver(matrix, preGen);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = executor.submit(() -> solver.solve());
		
		while(!future.isDone()) {
			updateGrid(matrix);
		}
		
		boolean solvable = false;
		try {
			solvable = future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!solvable) {
			JOptionPane.showMessageDialog(null, "Cannot solve sudoku.", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		updateGrid(matrix);

	}

	private Color getColor(int row, int col) {
		int result = (row / 3 + col / 3);
		if (result % 2 == 0) {
			return Color.CYAN;
		} else {
			return Color.WHITE;
		}
	}
	
	public void updateGrid(SudokuModel model) {
		int[][] grid;
		try {
			grid = model.readGrid();
		} catch (InterruptedException e) {
			System.out.println("Interrupted while updating UI");
			return;
		}
		SwingUtilities.invokeLater( () -> {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					textGrid[i][j].setText(String.valueOf(grid[i][j]));
				}
			}
		});
		
	}
}
