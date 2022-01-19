package sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import sudoku.buildlogic.SudokuBuildLogic;
import sudoku.userinterface.IUserInterfaceContract;
import sudoku.userinterface.StartPage;
import sudoku.userinterface.UserInterfaceImpl;

import java.io.IOException;

public class SudokuApplication extends Application {

    private IUserInterfaceContract.View uiImpl;
    private StartPage startPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        startPage = new StartPage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
