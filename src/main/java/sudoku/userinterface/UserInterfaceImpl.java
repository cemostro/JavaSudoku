package sudoku.userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sudoku.computationlogic.GameGenerator;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;
import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

import java.util.HashMap;

public class UserInterfaceImpl implements IUserInterfaceContract.View, EventHandler<KeyEvent> {

    //Background window for the application
    private final Stage stage;
    private final Group root;

    private final HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String SUDOKU = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        drawSolveButton(root);
        stage.show();
    }

    private void drawGridLines(Group root) {
        //TODO Fix 4*4 grid - Limit to 1-4
        double lineOffset = BOARD_X_AND_Y / GRID_BOUNDARY;
        double xAndY = 50 + lineOffset;
        int index = 0;
        int squareSize = (int) Math.sqrt(GRID_BOUNDARY);
        while (index < GRID_BOUNDARY - 1) {
            int thickness = 2;
            if ((index + 1) % squareSize == 0) {
                thickness = 3;
            }
            Rectangle verticalLine = getLine(xAndY + lineOffset * index, BOARD_PADDING, BOARD_X_AND_Y, thickness);
            Rectangle horizontalLine = getLine(BOARD_PADDING, xAndY + lineOffset * index, thickness, BOARD_X_AND_Y);

            //Add the UI elements to the group
            root.getChildren().addAll(verticalLine, horizontalLine);
            index++;
        }
    }

    private Rectangle getLine(double x, double y, double height, double width) {
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);
        return line;
    }

    private void drawTextFields(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;

        final int xAndYDelta = (int) (BOARD_X_AND_Y / GRID_BOUNDARY);

        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                int x = xOrigin + xIndex * xAndYDelta;
                int y = yOrigin + yIndex * xAndYDelta;

                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);

                styleSudokuTile(tile, x, y);

                //This will handle key pressed events
                tile.setOnKeyPressed(this);
                //Put textField in our hashMap
                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
        }
    }

    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);

        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);

        tile.setPrefHeight(BOARD_X_AND_Y / GRID_BOUNDARY);
        tile.setPrefWidth(BOARD_X_AND_Y / GRID_BOUNDARY);

        //Transparent background, will be supplied by sudokuBoard
        tile.setBackground(Background.EMPTY);
    }

    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);

        boardBackground.setFill(BOARD_BACKGROUND_COLOR);

        root.getChildren().add(boardBackground);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235,690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    private void drawSolveButton(Group root) {
        Button button = new Button("Show Solution");
        button.setLayoutX(480);
        button.setLayoutY(660);
        Font buttonFont = new Font(15);
        button.setFont(buttonFont);
        button.setStyle("-fx-background-color: lightgreen");
        button.setOnAction(actionEvent -> {
            this.updateBoard(new SudokuGame(GameState.COMPLETE, GameGenerator.getSolution()));
            showDialog("Do you want to play again?");
        });
        root.getChildren().add(button);

    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));

        String value = Integer.toString(input);

        if (value.equals("0"))
            value = "";

        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(game.getCopyOfGridState()[xIndex][yIndex]);

                if (value.equals("0"))
                    value = "";

                tile.setText(value);

                if (game.getGameState() == GameState.NEW) {
                    //If field is empty, make it more visible to indicate that it can be changed
                    //Otherwise make pre-existing values less opaque. CSS is used.
                    if (value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        //To ask if the user wants to start a new game
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK)
            listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public void handle(KeyEvent event) {
        //When the user inputs a text box, that event will pop up here
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            String valueRegex = "";
            if (GRID_BOUNDARY == 4)
                valueRegex = "[0-4]";
            else if (GRID_BOUNDARY == 9)
                valueRegex = "[0-9]";
            if (event.getText().matches(valueRegex)) {
                int value = Integer.parseInt(event.getText());
                //Source is the ui element, clicked object
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                //Backspace clears entry
                handleInput (0, event.getSource());
            } else {
                //If the user types anything else we just set it to empty
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume();
    }

    private void handleInput(int value, Object source) {
        listener.onSudokuInput(((SudokuTextField) source).getX(), ((SudokuTextField) source).getY(), value);

    }
}
