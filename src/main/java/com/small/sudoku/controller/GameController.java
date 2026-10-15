package com.small.sudoku.controller;

import com.small.sudoku.generator.SudokuGenerator;
import com.small.sudoku.model.GameSession;
import com.small.sudoku.model.Sudoku;
import com.small.sudoku.model.SudokuCell;
import com.small.sudoku.model.emun.Difficulty;
import com.small.sudoku.solver.SudokuSolver;
import com.small.sudoku.utilities.SudokuRuleChecker;

import java.util.*;

public class GameController {
    private final Scanner scanner = new Scanner(System.in);
    private final SudokuGenerator generator = new SudokuGenerator();
    private final SudokuSolver solver = new SudokuSolver();
    private final SudokuRuleChecker ruleChecker = new SudokuRuleChecker();
    private GameSession session;
    private Sudoku sudoku;


    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("===== SUDOKU =====");
            System.out.println("1. Start New Game");
            System.out.println("2. Solve Automatically");
            System.out.println("3. Exit");
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1 -> startNewGame();
                case 2 -> solveAuto();
                case 3 -> {
                    running= false;
                    System.exit(0);
                }
            }
        }
    }

    private void solveAuto() {
        Difficulty gameDifficulty= selectDifficulty();
        SudokuCell[][] puzzle =generator.generatePuzzle(gameDifficulty);
        Sudoku sudoku = new Sudoku();
        sudoku.setBoard(puzzle);
        System.out.println("Original Puzzle:");
        sudoku.printBoard();
        if (solver.solve(puzzle)) {
            System.out.println("\nSolved Puzzle:");
            sudoku.printBoard();
        } else {
            System.out.println("No solution found.");
        }
    }

    private void startNewGame() {
        Difficulty gameDifficulty= selectDifficulty();
        sudoku= new Sudoku();
        sudoku.setBoard(generator.generatePuzzle(gameDifficulty));
        session = new GameSession();
        session.startTimer();
        System.out.println("Game Start!!");
        playGame();
    }


    private void playGame() {
        boolean playing = true;

        while (playing) {
            System.out.println("\n=== SUDOKU BOARD ===");
            sudoku.printBoard();
            System.out.println("Time: " + session.getFormattedTime());

            System.out.println("\nEnter: row column number");
            System.out.println("Example: 1 3 4");
            System.out.println("Type 'b' to open the menu.");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("b")) {
                playing = showGameMenu();
                continue;
            }

            String[] values = input.split("\\s+");

            if (values.length != 3) {
                System.out.println("Enter three numbers, e.g. 1 3 4.");
                continue;
            }

            try {
                int row = Integer.parseInt(values[0]);
                int col = Integer.parseInt(values[1]);
                int number = Integer.parseInt(values[2]);

                enterNumber(row, col, number);

                if (isBoardCompleteAndValid()) {
                    finishGame();
                    playing = false;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Use numbers 1-9 or 'b'.");
            }
        }
    }

    private boolean showGameMenu() {

        while (true) {
            System.out.println("\n===== GAME MENU =====");
            System.out.println("1. Erase Number");
            System.out.println("2. Show Time");
            System.out.println("3. Give Up");
            System.out.println("4. Return to Main Menu");
            System.out.println("5. Continue Playing");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    eraseNumber();
                    return true;
                }
                case "2" -> {
                    System.out.println("Time: " + session.getFormattedTime());
                    return true;
                }
                case "3" -> {
                    session.stopTimer();
                    System.out.println("You gave up:(");
                    System.out.println("Time: " + session.getFormattedTime());

                    if (solver.solve(sudoku.getBoard())) {
                        System.out.println("\nSolution:");
                        sudoku.printBoard();
                    } else {
                        System.out.println("Could not solve the current board.");
                    }
                    return false;
                }
                case "4" -> {
                    session.stopTimer();
                    System.out.println("Returning to main menu...");
                    return false;
                }
                case "5", "b" -> {
                    return true;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }


    private void enterNumber(int row, int col, int number){
        if (row < 1 || row > 9 || col < 1 || col > 9 || number < 1 || number > 9) {
            System.out.println("Row, column, and number must be between 1 and 9.");
            return;
        }

        row--;
        col--;

        SudokuCell[][] board = sudoku.getBoard();
        SudokuCell cell = board[row][col];

        if (cell.isFixed()) {
            System.out.println("You cannot modify an original clue!");
            return;
        }

        if (cell.getValue() != 0) {
            System.out.println("Erase the current value first.");
            return;
        }

        if (!ruleChecker.isValidPlacement(board, row, col, number)) {
            System.out.println("Invalid move!");
            return;
        }
        cell.setValue(number);
        sudoku.printBoard();
    }



    private void eraseNumber() {
        System.out.print("Enter row and column (e.g. 1 3): ");
        String input = scanner.nextLine().trim();
        String[] values = input.split("\\s+");
        if (values.length != 2) {
            System.out.println("Please enter row and column.");
            return;
        }
        try {
            int row = Integer.parseInt(values[0]);
            int col = Integer.parseInt(values[1]);
            if (row < 1 || row > 9 || col < 1 || col > 9) {
                System.out.println("Invalid row or column.");
                return;
            }

            SudokuCell cell = sudoku.getBoard()[row - 1][col - 1];
            if (cell.isFixed()) {
                System.out.println("You cannot erase an original clue!");
                return;
            }
            cell.setValue(0);
            System.out.println("Cell cleared.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        }
    }


    private boolean isBoardCompleteAndValid() {
        SudokuCell[][] board = sudoku.getBoard();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board[row][col].getValue();
                if (value < 1 || value > 9) {
                    return false;
                }

                board[row][col].setValue(0);
                boolean valid;
                try {
                    valid = ruleChecker.isValidPlacement(board, row, col, value);
                } finally {
                    board[row][col].setValue(value);
                }


                if (!valid) {
                    return false;
                }
            }
        }

        return true;
    }
    private void finishGame() {
        session.stopTimer();

        System.out.println("\n============================");
        System.out.println("     CONGRATULATIONS!");
        System.out.println("============================");
        System.out.println("You completed the Sudoku!");
        System.out.println("Time: " + session.getFormattedTime());
        sudoku.printBoard();
    }

    private Difficulty selectDifficulty() {
        while (true) {
            System.out.println("Choose a Difficulty:");
            System.out.println("1. Easy");
            System.out.println("1. Medium");
            System.out.println("3. Hard");
            System.out.println("4. Master");

            int choice = scanner.nextInt();

            Difficulty puzzleDifficulty = switch (choice) {
                case 1 -> Difficulty.EASY;
                case 2 -> Difficulty.MEDIUM;
                case 3 -> Difficulty.HARD;
                case 4 -> Difficulty.MASTER;
                default -> null;
            };
            return puzzleDifficulty;
        }
    }
}
