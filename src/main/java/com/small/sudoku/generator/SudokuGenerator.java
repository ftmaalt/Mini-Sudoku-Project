package com.small.sudoku.generator;

import com.small.sudoku.model.Sudoku;
import com.small.sudoku.model.SudokuCell;
import com.small.sudoku.model.emun.Difficulty;
import com.small.sudoku.solver.SudokuSolver;
import com.small.sudoku.utilities.SudokuRuleChecker;

import java.util.*;

public class SudokuGenerator {
    private final SudokuSolver solver = new SudokuSolver();
    private final SudokuRuleChecker ruleChecker = new SudokuRuleChecker();
    private final Random random = new Random();

    public SudokuCell[][] generateCompleteBoard() {
        Sudoku sudoku = new Sudoku();
        SudokuCell[][] board = sudoku.getBoard();
        boardFiller(board);
        if (!boardFiller(board)) {
            throw new IllegalStateException(
                    "Unable to generate a complete Sudoku board."
            );
        }
        return board;
    }

    public boolean boardFiller(SudokuCell[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getValue() == 0) {
                    List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
                    Collections.shuffle(numbers, random);
                    for (int number : numbers) {
                        if (ruleChecker.isValidPlacement(board, row, col, number)) {
                            board[row][col].setValue(number);
                            if (boardFiller(board))
                                return true;
                            board[row][col].setValue(0);
                        }
                    }
                    return false;
                }
            }
        }
        return true;

    }

    public SudokuCell[][] generatePuzzle(Difficulty difficulty) {
        SudokuCell[][] board = generateCompleteBoard();
        int removed = 0;
        int cellsToRemove = switch (difficulty) {
            case EASY -> 36;
            case MEDIUM -> 44;
            case HARD -> 50;
            case MASTER -> 55;
        };
        while (removed < cellsToRemove) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (board[row][col].getValue() != 0) {
                board[row][col].setValue(0);
                removed++;
            }
        }
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getValue() != 0) {
                    board[row][col].setFixed(true);
                }
            }
        }
        return board;
    }
}

