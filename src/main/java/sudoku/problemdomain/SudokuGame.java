package sudoku.problemdomain;

import sudoku.constants.GameState;
import sudoku.computationlogic.SudokuUtilities;

import java.io.Serializable;

public class SudokuGame implements Serializable {
    private final GameState gameState;
    protected final int[][] gridState;

    public static int GRID_BOUNDARY = 9;

    public SudokuGame(GameState gameState, int[][] gridState) {
        this.gameState = gameState;
        this.gridState = gridState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int[][] getCopyOfGridState() {
        return SudokuUtilities.copyToNewArray(gridState);
    }
}
