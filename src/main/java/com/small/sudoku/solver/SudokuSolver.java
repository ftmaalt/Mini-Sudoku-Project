
package com.small.sudoku.solver;

import com.small.sudoku.model.SudokuCell;
import com.small.sudoku.utilities.SudokuRuleChecker;

public class SudokuSolver {

    private final SudokuRuleChecker ruleChecker = new SudokuRuleChecker();

    public boolean solve(SudokuCell[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getValue() == 0) {
                    for (int number = 1; number <= 9; number++) {
                        if (ruleChecker.isValidPlacement(board, row, col, number)) {
                            board[row][col].setValue(number);
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col].setValue(0);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public int countSolutions(SudokuCell[][] board, int limit) {
        if (limit <= 0) {
            return 0;
        }
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getValue() == 0) {
                    int count = 0;
                    for (int num = 1; num <= 9; num++) {
                        if (ruleChecker.isValidPlacement(board, row, col, num)) {
                            board[row][col].setValue(num);
                            count += countSolutions(board, limit - count);
                            board[row][col].setValue(0);
                            if (count >= limit) {
                                return count;
                            }
                        }
                    }
                    return count;
                }
            }
        }
        return 1;
    }
}

