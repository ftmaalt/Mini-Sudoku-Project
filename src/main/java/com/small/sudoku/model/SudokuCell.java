package com.small.sudoku.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SudokuCell {
    private int row;
    private int col;
//    private int[] guesses; // possible correct values for the cell
    @Setter
    private int value;
    @Setter
    private boolean isFixed; // if true then the cell cannot be changed

    public SudokuCell(int row, int col, int value) {
        this.row = row;
        this.col = col;
            if (value<=0 || value >9)
                this.value = 0;
            else
                this.value = value;
        if (value == 0)
            this.isFixed= false;
        else
            this.isFixed = true;
    }

}
