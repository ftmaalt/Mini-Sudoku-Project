
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
}
