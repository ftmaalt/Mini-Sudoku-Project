package com.small.sudoku.model;

import com.small.sudoku.model.emun.Difficulty;

import java.time.Duration;
import java.time.Instant;

public class GameSession {
    private Sudoku sudoku;
    private Difficulty difficulty;
    private Instant startTime;
    private Instant endTime;
    private boolean completed;

    public void startTimer(){
        startTime = Instant.now();
        endTime = null;
    }
    public void stopTimer(){
        if (startTime!=null && endTime==null){
            endTime = Instant.now();
        }
    }

    public long getElapsedSeconds() {

        if (startTime == null) {
            return 0;
        }
        Instant finish = endTime != null ? endTime : Instant.now();
        return Duration.between(startTime, finish).getSeconds();
    }

    public String getFormattedTime(){
        long elapsed = getElapsedSeconds();
        long minutes = elapsed / 60;
        long seconds = elapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


}
