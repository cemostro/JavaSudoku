package sudoku.computationlogic;

import sudoku.constants.GameState;
import sudoku.constants.Rows;
import sudoku.problemdomain.SudokuGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameLogic {

    public static SudokuGame getNewGame() {
        return new SudokuGame(GameState.NEW, GameGenerator.getNewGameGrid());
    }

    public static GameState checkForCompletion(int[][] grid) {
        if (sudokuIsInvalid(grid))
            return GameState.ACTIVE;
        if (tilesAreNotFilled(grid))
            return GameState.ACTIVE;
        return GameState.COMPLETE;
    }

    public static boolean sudokuIsInvalid(int[][] grid) {
        if (rowOrColumnsAreInvalid(grid, true))
            return true;
        if (rowOrColumnsAreInvalid(grid, false))
            return true;
        if (squaresAreInvalid(grid))
            return true;
        return false;
    }

    private static boolean rowOrColumnsAreInvalid (int[][] grid, boolean checkRow) {
        for (int i = 0; i < GRID_BOUNDARY; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < GRID_BOUNDARY ; j++) {
                if (checkRow)
                    row.add(grid[j][i]);
                else
                    row.add(grid[i][j]);
            }

            if (collectionHasRepeats(row))
                return true;
        }
        return false;
    }

    private static boolean squaresAreInvalid(int[][] grid) {
        if (rowOfSquaresIsInvalid(Rows.TOP, grid))
            return true;
        if (rowOfSquaresIsInvalid(Rows.MIDDLE, grid))
            return true;
        if (rowOfSquaresIsInvalid(Rows.BOTTOM, grid))
            return true;
        return false;
    }

    private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid) {
        int squareSize = (int) Math.sqrt(GRID_BOUNDARY);
        switch (value) {
            case TOP -> {
                if (squareIsInvalid(0, 0, grid))
                    return true;
                if (squareIsInvalid(squareSize, 0, grid))
                    return true;
                if (squareSize > 2 && squareIsInvalid(squareSize*2, 0, grid))
                    return true;
                return false;
            }
            case MIDDLE -> {
                if (squareIsInvalid(0, squareSize, grid))
                    return true;
                if (squareIsInvalid(squareSize, squareSize, grid))
                    return true;
                if (squareSize > 2 && squareIsInvalid(squareSize*2, squareSize, grid))
                    return true;
                return false;
            }
            case BOTTOM -> {
                if (squareSize > 2 && squareIsInvalid(0, squareSize*2, grid))
                    return true;
                if (squareSize > 2 && squareIsInvalid(squareSize, squareSize*2, grid))
                    return true;
                if (squareSize > 2 && squareIsInvalid(squareSize*2, squareSize*2, grid))
                    return true;
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean squareIsInvalid(int xIndex, int yIndex, int[][] grid) {
        int squareSize = (int) Math.sqrt(GRID_BOUNDARY);
        int yIndexEnd = yIndex + squareSize;
        int xIndexEnd = xIndex + squareSize;
        List<Integer> square = new ArrayList<>();
        while (yIndex < yIndexEnd) {
            while (xIndex < xIndexEnd) {
                square.add(grid[xIndex][yIndex]);

                xIndex++;
            }
            xIndex -= squareSize;
            yIndex++;
        }
        return collectionHasRepeats(square);
    }

    public static boolean collectionHasRepeats(List<Integer> collection) {
        for (int index = 1; index <= GRID_BOUNDARY; index++) {
            if (Collections.frequency(collection, index) > 1)
                return true;
        }
        return false;
    }

    public static boolean tilesAreNotFilled(int[][] grid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                if (grid[xIndex][yIndex] == 0)
                    return true;
            }
        }
        return false;
    }
}
