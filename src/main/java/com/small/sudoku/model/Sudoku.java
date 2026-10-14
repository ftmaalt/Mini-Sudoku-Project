package com.small.sudoku.model;
import com.small.sudoku.exception.InvalidCharacterException;
import com.small.sudoku.exception.MissingFileException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Sudoku {
    private SudokuCell[][] board;
    private static final int SIZE = 9;
    public static final String CYAN = "\u001B[36m";
    public static final String RESET = "\u001B[0m";
    public Sudoku(){
        board= new SudokuCell[SIZE][SIZE];
        for(int row=0; row< board.length; row++){
            for(int col=0; col< board[row].length; col++){
                board[row][col]= new SudokuCell(row, col, 0 );
            }
        }
    }
    public void loadPuzzle(String filename) {

        String filePath = "samples/unsolved/" + filename;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new MissingFileException("Puzzle not found: " + filename);
        }
        try (BufferedReader puzzleReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            int row = 0;

            while ((line = puzzleReader.readLine()) != null) {
                for (int col = 0; col < SIZE; col++) {
                    char number = line.charAt(col);
                    if (number < '0' || number > '9') {
                        throw new InvalidCharacterException("Invalid character '" + number + "' at row " + (row + 1) + ", column " + (col + 1));
                    }
                    int num = Character.getNumericValue(number);
                    board[row][col].setValue(num);
                }
                row++;
            }

            System.out.println(filename+" loaded successfully!");
        } catch (IOException e) {
            throw new RuntimeException("Error reading puzzle: " + e.getMessage(), e);
        }
    }
    public void printBoard(){
        String border ="+-------+-------+-------+";
        for(int row=0; row< board.length; row++) {
            if (row % 3 == 0)
                System.out.println(border);
            System.out.print("| ");
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getValue() !=0)
                    System.out.print(CYAN+board[row][col].getValue() +" "+ RESET);
                else
                System.out.print(board[row][col].getValue() +" ");
                if ((col +1) %3 == 0)
                    System.out.print("| ");
            }
            System.out.println();
        }


    }
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.loadPuzzle("puzzle1.txt");
        sudoku.printBoard();
    }
}
