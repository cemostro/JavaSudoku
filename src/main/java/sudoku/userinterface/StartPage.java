package sudoku.userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sudoku.buildlogic.SudokuBuildLogic;
import sudoku.problemdomain.SudokuGame;

import java.io.IOException;
import java.util.HashMap;

public class StartPage implements EventHandler<ActionEvent> {

    //Background window for the application
    private final Stage stage;
    private final Group root;
    private int gameSize = 9;

    public StartPage(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        initializeMainPage();
    }

    private void initializeMainPage() {
        drawBackground(root);
        drawTitle(root);
        drawStartButton(root);
        drawDropdownMenu(root);
        stage.show();
    }

    private void drawDropdownMenu(Group root) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("4*4 (Easy)");
        comboBox.getItems().add("9*9 (Normal)");
        comboBox.getItems().add("16*16 (Hard) (TO BE IMPLEMENTED)");
        comboBox.getSelectionModel().select("9*9 (Normal)");
        comboBox.setLayoutX(215);
        comboBox.setPrefWidth(120);
        comboBox.setLayoutY(250);
        comboBox.setOnAction(actionEvent -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            switch (selectedItem) {
                case "4*4 (Easy)" -> gameSize = 4;
                case "9*9 (Normal)", "16*16 (Hard) (TO BE IMPLEMENTED)" -> gameSize = 9;
            }
        });
        root.getChildren().add(comboBox);
    }

    private void drawStartButton(Group root) {
        Button button = new Button("Start Game");
        button.setLayoutX(200);
        button.setLayoutY(300);
        button.setAlignment(Pos.BOTTOM_CENTER);
        Font buttonFont = new Font(25);
        button.setFont(buttonFont);
        button.setStyle("-fx-background-color: lightgreen");
        button.setOnAction(this);
        root.getChildren().add(button);
    }

    private void drawTitle(Group root) {
        Text title = new Text(100,180, "Welcome to Sudoku");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, 550, 450);
        scene.setFill(Color.rgb(0, 150, 136));
        stage.setScene(scene);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        stage.close();
        SudokuGame.GRID_BOUNDARY = gameSize;
        IUserInterfaceContract.View uiImpl = new UserInterfaceImpl(new Stage());
        try {
            SudokuBuildLogic.build(uiImpl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionEvent.consume();
    }
}
