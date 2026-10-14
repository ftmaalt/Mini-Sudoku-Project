package com.small.sudoku.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SudokuCell {
    private int row;
    private int col;
    private int[] guesses; // possible correct values for the cell
    @Setter
    private int value;
    @Setter
    private boolean isFixed; // if true then the cell cannot be changed
    private int[] validInput={0,1,2,3,4,5,6,7,8,9};

    public SudokuCell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        for (int i =0; i< validInput.length; i++) {
            if (value == validInput[i])
                this.value = value;
            else
                this.value = 0;
        }
        if (value == 0)
            this.isFixed= false;
        else
            this.isFixed = true;
    }

}
