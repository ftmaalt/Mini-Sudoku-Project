package com.small.sudoku.model;
import com.small.sudoku.exception.InvalidCharacterException;
import com.small.sudoku.exception.MissingFileException;
import com.small.sudoku.generator.SudokuGenerator;
import com.small.sudoku.model.emun.Difficulty;
import com.small.sudoku.solver.SudokuSolver;
import lombok.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
@Data
public class Sudoku {

    private SudokuCell[][] board;
    private static final int SIZE = 9;
    public static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public Sudoku(){
        board= new SudokuCell[SIZE][SIZE];
        for(int row=0; row< board.length; row++){
            for(int col=0; col< board[row].length; col++){
                board[row][col]= new SudokuCell(row, col, 0 );
            }
        }
    }
    public void loadPuzzle(String filename, Difficulty difficulty) {
        String filePath="";
        if (difficulty.equals(Difficulty.EASY)){
            filePath = "samples/unsolved/easy/" + filename;
        }else if (difficulty.equals(Difficulty.MEDIUM)){
            filePath = "samples/unsolved/medium/" + filename;
        }else if(difficulty.equals(Difficulty.HARD)){
            filePath = "samples/unsolved/hard/" + filename;
        } else if(difficulty.equals(Difficulty.MASTER)){
            filePath = "samples/unsolved/master/" + filename;
        }else {
            throw new MissingFileException("No puzzles with difficulty are found");
        }

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
                    board[row][col].setFixed(num != 0);
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
                SudokuCell cell = board[row][col];
                if (!cell.isFixed() && cell.getValue() != 0) {
                    System.out.print(GREEN + cell.getValue() + RESET + " ");
                } else if (cell.isFixed() && cell.getValue()!=0){
                    System.out.print(CYAN+board[row][col].getValue() +" "+ RESET);
                }
                else
                System.out.print(board[row][col].getValue() +" ");
                if ((col +1) %3 == 0)
                    System.out.print("| ");
            }
            System.out.println();
        }
        System.out.println(border);
    }


    public void saveSolution(String filename) throws IOException {
        String solutionFilename = filename.replace(".txt", ".solution.txt");
        Path outputDirectory = Path.of("solutions");
        Files.createDirectories(outputDirectory);
        Path outputPath = outputDirectory.resolve(solutionFilename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    writer.write(String.valueOf(board[row][col].getValue()));
                }
                writer.newLine();
            }
        }
        System.out.println("Solution saved successfully to: " + outputPath);
    }
    public static void main(String[] args) {
//        Sudoku sudoku = new Sudoku();
//        sudoku.loadPuzzle("puzzle1.txt", Difficulty.EASY);
//        System.out.println("Original Puzzle:");
//        sudoku.printBoard();
//        SudokuSolver solver = new SudokuSolver();
//
//        if (solver.solve(sudoku.getBoard())) {
//            System.out.println("Solved Puzzle:");
//            sudoku.printBoard();
//            try {
//                sudoku.saveSolution("puzzle1.txt");
//            } catch (IOException e) {
//                System.out.println("Error saving solution: " + e.getMessage());
//            }
//        } else {
//            System.out.println("No solution found.");
//        }
//
//
        SudokuGenerator generator = new SudokuGenerator();

        SudokuCell[][] generatedBoard =
                generator.generatePuzzle(Difficulty.MASTER);

        Sudoku sudoku = new Sudoku();
        sudoku.setBoard(generatedBoard);

        System.out.println("Generated MASTER Puzzle:");
        sudoku.printBoard();
    }
}
