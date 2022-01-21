package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;

import java.util.*;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameGenerator {

    public static int[][] getNewGameGrid() {
        return unsolveGame(getSolvedGame());
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;
        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        while (!solvable) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            //Remove 40 values
            int totalSize = GRID_BOUNDARY * GRID_BOUNDARY;
            while (index < (totalSize/2)) {
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }
            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    private static int[][] getSolvedGame() {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= GRID_BOUNDARY; i++) {
            values.add(i);
        }
        int[][] solvedBoard = new int[GRID_BOUNDARY][GRID_BOUNDARY];
        getSolvedGameHelper(0, 0, solvedBoard, values);
        return solvedBoard;
    }

    private static boolean getSolvedGameHelper(int xIndex, int yIndex, int[][] board, List<Integer> randomValues) {
        if (yIndex == GRID_BOUNDARY)
            return true;
        Collections.shuffle(randomValues);
        //Copy random numbers to local array so that they don't get reshuffled in next recursion level.
        Integer[] currentRandomValues = randomValues.toArray(new Integer[0]);
        for (int i = 0; i < GRID_BOUNDARY; i++) {
            board[xIndex][yIndex] = currentRandomValues[i];
            if (GameLogic.sudokuIsInvalid(board)) {
                continue;
            }
            boolean isSolved;
            if (xIndex == GRID_BOUNDARY - 1) {
                isSolved = getSolvedGameHelper(0, yIndex+1, board, randomValues);
            } else {
                isSolved = getSolvedGameHelper(xIndex + 1, yIndex, board, randomValues);
            }
            if (isSolved)
                return true;
        }
        board[xIndex][yIndex] = 0;
        return false;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
