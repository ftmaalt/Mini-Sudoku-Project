package com.small.sudoku.utilities;

import com.small.sudoku.model.SudokuCell;

public class SudokuRuleChecker {
    public boolean isValidRowOrColumn(SudokuCell[][] board, int row, int col, int number) {
        for (int i = 0; i < board.length; i++) {
            if (board[row][i].getValue() == number) {
                return false;
            }
            if (board[i][col].getValue() == number) {
                return false;
            }
        }
        return true;
    }
    public boolean isValidSubGrid(SudokuCell[][] board, int row, int col, int number) {
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c].getValue() == number) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidPlacement(SudokuCell[][] board, int row, int col, int number) {
        return isValidRowOrColumn(board, row, col, number)
                && isValidSubGrid(board, row, col, number);
    }
}
