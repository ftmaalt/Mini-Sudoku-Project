package com.small.sudoku.exception;

public class MissingFileException extends RuntimeException {
    public MissingFileException(String message) {
        super(message);
    }
}
