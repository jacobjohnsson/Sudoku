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
	SudokuModel model = new SudokuModel();
	
	private int SIMULATION_SLOWDOWN = 20;
	
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
		JTextField slowdownField = new JTextField("SlowDown (0 - 1000)");
		JButton slowdownButton = new JButton("Set");
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(clearButton);
		buttonPanel.add(solveButton);
		buttonPanel.add(slowdownField);
		buttonPanel.add(slowdownButton);
		pane.add(buttonPanel, BorderLayout.PAGE_END);
		
		clearButton.addActionListener(event -> clear());
		solveButton.addActionListener(event -> {
			clearButton.setEnabled(false);
			slowdownButton.setEnabled(false);
			solveButton.setEnabled(false);
			new Thread (() -> {
				solve(); 
				clearButton.setEnabled(true);
				slowdownButton.setEnabled(true);
				solveButton.setEnabled(true);
			}).start();
		});
		slowdownButton.addActionListener(event -> SIMULATION_SLOWDOWN = Integer.parseInt(slowdownField.getText())); 
		
		JPanel jGrid = new JPanel(new GridLayout(9,9,5,5));
		
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				JLimitedTextField textField = new JLimitedTextField(1);
				textField.setPreferredSize(new Dimension(30, 30));
				textField.setBackground(getColor(row, col));
				textGrid[row][col] = textField;
				jGrid.add(textField);
				model.subscribe(row,  col, textField);
			}
		}
		
		pane.add(jGrid);
		
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
		
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (textGrid[row][col].getText().equals("")) {
					model.put(row, col, 0);
				} else {
					model.put(row, col, Integer.parseInt(textGrid[row][col].getText()));
					preGen[row][col] = true;
				}
			}
		}
		
		Solver solver = new Solver(model, preGen, SIMULATION_SLOWDOWN);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = executor.submit(() -> solver.solve());
		
		boolean solvable = false;
		try {
			solvable = future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!solvable) {
			JOptionPane.showMessageDialog(null, "Cannot solve sudoku.", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		updateGrid(model);

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
