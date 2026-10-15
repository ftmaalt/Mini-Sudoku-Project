# Sudoku — Java & Spring Boot Mini Project

A console-based Sudoku application built with **Java 17** and **Spring Boot**. It can load and solve existing 9×9 puzzles using recursive backtracking, generate randomized puzzles, and let players complete a puzzle interactively while tracking their time.

## Features

- **Sudoku solver:** Solves supplied puzzles with recursive backtracking and validates placements against row, column, and 3×3-box rules.
- **Puzzle loading:** Reads 9×9 puzzles from text resources (with `0` representing an empty cell).
- **Puzzle generation:** Fills a board using randomized backtracking, then removes clues while checking that the puzzle still has a unique solution.
- **Difficulty choices:** Easy, Medium, Hard, and Master, currently based on target clue counts rather than measured human solving difficulty.
- **Interactive console gameplay:** Enter numbers by row and column, erase editable values, and open the game menu by typing `b`.
- **Timer:** Tracks elapsed game time and reports the completion or exit time.
- **Colored terminal board:** Highlights fixed clues in pink and filled-in numbers in matcha green in terminals that support ANSI true color.
- **Text output:** Displays the board as a 9×9 ASCII grid and can save solved puzzles to files.
- **Error handling:** Includes custom exceptions for invalid characters and missing puzzle files.

## Tech stack

- Java 17
- Spring Boot / Maven
- Lombok
- Java standard library (`java.io`, `java.nio.file`, `java.time`, collections)

> The gameplay interface is console-based. Although the project uses Spring Boot, the game does not require a web frontend or REST endpoints.

## Project structure

```text
src/main/
├── java/com/small/sudoku/
│   ├── controller/
│   │   └── GameController.java
│   ├── generator/
│   │   └── SudokuGenerator.java
│   ├── model/
│   │   ├── Sudoku.java
│   │   ├── SudokuCell.java
│   │   ├── GameSession.java
│   │   └── emun/
│   │       └── Difficulty.java
│   ├── solver/
│   │   └── SudokuSolver.java
│   ├── utilities/
│   │   └── SudokuRuleChecker.java
│   └── exception/
│       ├── InvalidCharacterException.java
│       └── MissingFileException.java
└── resources/
    └── samples/unsolved/
        ├── easy/
        ├── medium/
        ├── hard/
        └── master/
solutions/               # Saved solution files, when generated
```

The structure reflects the classes discussed during development. Adjust folders or filenames if your repository differs.

## Getting started

### Prerequisites

- JDK 17 or a version compatible with the project's `pom.xml`
- Maven (or the included Maven wrapper, if present)
- IntelliJ IDEA or another Java IDE (optional)

### Run

1. Clone your repository and open it as a Maven project.
2. Configure the project's JDK and reload Maven dependencies.
3. Run the application entry point that launches `new GameController().start()`. In the current development setup, this is invoked from `Sudoku.main()`.

Alternatively, after compiling with Maven, launch the configured application main class from your IDE. The console will display the game menu.

## How to play

At launch, select one of the main menu options:

```text
===== SUDOKU =====
1. Start New Game
2. Solve Automatically
3. Exit
```

For a new game, choose **Easy**, **Medium**, **Hard**, or **Master**. The game generates a puzzle and starts the timer.

Enter a move as three space-separated values:

```text
row column number
1 3 4
```

This places `4` in **row 1, column 3**. Player-facing coordinates are 1–9; Java array indexes are 0–8. Original clues cannot be edited.

Type **`b`** during number entry to open the game menu, where you can erase a value, view the timer, give up, or return to the main menu.

> A move may satisfy the immediate Sudoku constraints without being part of the puzzle's unique solution. The completed board must pass the full-board validity check to win.

## Puzzle format

Each input file contains **nine rows**, each with **nine digits** from `0` to `9`. Zero means empty.

```text
530070000
600195000
098000060
800060003
400803001
700020006
060000280
000419005
000080079
```

An example loaded board looks like:

```text
+-------+-------+-------+
| 5 3 0 | 0 7 0 | 0 0 0 |
| 6 0 0 | 1 9 5 | 0 0 0 |
| 0 9 8 | 0 0 0 | 0 6 0 |
+-------+-------+-------+
| 8 0 0 | 0 6 0 | 0 0 3 |
| 4 0 0 | 8 0 3 | 0 0 1 |
| 7 0 0 | 0 2 0 | 0 0 6 |
+-------+-------+-------+
| 0 6 0 | 0 0 0 | 2 8 0 |
| 0 0 0 | 4 1 9 | 0 0 5 |
| 0 0 0 | 0 8 0 | 0 7 9 |
+-------+-------+-------+
```

## How it works

### Solving

`SudokuSolver.solve()` finds the next empty cell, attempts values 1–9, and checks each candidate using `SudokuRuleChecker`. When a candidate leads to a dead end, it resets the cell to `0` and tries the next candidate.

### Generating

`SudokuGenerator.boardFiller()` uses shuffled candidates with recursive backtracking to produce a completed valid grid. `generatePuzzle()` attempts to remove clues in random order; after each removal, `SudokuSolver.countSolutions(board, 2)` checks uniqueness. A removal is accepted only if the puzzle retains exactly one solution.

The generator may finish with more clues than its target if further removals would make the puzzle non-unique. Difficulty names currently indicate approximate clue-count targets, not a guarantee of human difficulty.

### Timer

`GameSession` records game start/end times and calculates elapsed duration with `Instant` and `Duration`.

## Current limitations and future enhancements

- Improve difficulty grading using required solving techniques, rather than clue count alone.
- Add a hint system and error highlighting for incorrect guesses.
- Add scoring based on difficulty, elapsed time, and hints used.
- Allow saving/loading an unfinished game session.
- Preserve a separate canonical solution throughout gameplay, so **Give Up** reliably reveals it even if a player has entered locally valid but ultimately incorrect values.
- Add automated unit tests for loading, invalid input, solving, uniqueness, and generation.

## Learning outcomes

This mini project demonstrates **object-oriented programming**, encapsulation, recursion and backtracking, 2D arrays, file I/O, custom exceptions, enums, input validation, and separation of concerns with a controller/model/solver structure.
