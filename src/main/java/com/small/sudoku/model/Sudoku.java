package com.small.sudoku.model;
import java.util.*;
public class Sudoku {
    private SudokuCell[][] board;
    private static final int SIZE = 9;

    public Sudoku(){
        board= new SudokuCell[SIZE][SIZE];
        for(int row=0; row< board.length; row++){
            for(int col=0; col< board[row].length; col++){
                board[row][col]= new SudokuCell(row, col, 0 );
            }
        }
    }

    public void printBoard(){
        String border ="+-------+-------+-------+";

        for(int row=0; row< board.length; row++) {
            if (row % 3 == 0)
                System.out.println(border);
            System.out.print("| ");
            for (int col = 0; col < board[row].length; col++) {
                System.out.print(board[row][col].getValue() +" ");
                if ((col +1) %3 == 0)
                    System.out.print("| ");
            }
            System.out.println();
        }
        System.out.println(border);

    }
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.printBoard();
    }
}
