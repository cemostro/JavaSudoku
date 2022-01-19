package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;
import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

import java.util.Arrays;

public class SudokuSolver {

    public static boolean puzzleIsSolvable(int[][] puzzle) {
        Coordinates[] emptyCells = typeWriterEnumerate(puzzle);

        int index = 0;
        int input = 0;

        while (index < (GRID_BOUNDARY + 1)) {
            Coordinates current = emptyCells[index];
            input = 1;

            int totalSize = GRID_BOUNDARY * GRID_BOUNDARY;
            while (input < (totalSize/2)) {
                puzzle[current.getX()][current.getY()] = input;

                if (GameLogic.sudokuIsInvalid(puzzle)) {
                    if (index == 0 && input == GRID_BOUNDARY) {
                        return false;
                    } else if (input == GRID_BOUNDARY){
                        index--;
                    }
                    input++;
                } else {
                    index++;

                    if (index == (totalSize/2) - 1)
                        return true;

                    input = GRID_BOUNDARY + 1;
                }
            }
        }
        return false;
    }

    private static Coordinates[] typeWriterEnumerate(int[][] puzzle) {
        int totalSize = GRID_BOUNDARY*GRID_BOUNDARY;
        Coordinates[] emptyCells = new Coordinates[GRID_BOUNDARY/2];
        int iterator = 0;
        for (int y = 0; y < GRID_BOUNDARY; y++) {
            for (int x = 0; x < GRID_BOUNDARY; x++) {
                if (puzzle[x][y] == 0) {
                    emptyCells[iterator] = new Coordinates(x, y);
                    if (iterator == (GRID_BOUNDARY/2) - 1)
                        return emptyCells;
                    iterator++;
                }
            }
        }
        return emptyCells;
    }
}
