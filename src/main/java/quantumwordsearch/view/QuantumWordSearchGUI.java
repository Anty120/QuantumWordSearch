package quantumwordsearch.view;

import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import quantumwordsearch.model.QuantumWordSearch;
import quantumwordsearch.model.Tile;

public class QuantumWordSearchGUI extends Application {
    private QuantumWordSearch qws;
    private GridPane grid = new GridPane();
    private GridPane wordList = new GridPane();
    private Tile[][] board;

    @Override
    public void start(Stage stage) throws Exception {
        // get data for the game from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        String filename = scanner.nextLine();
        scanner.close();
        // begin game
        qws = new QuantumWordSearch(filename);
        // make word search board
        grid.setAlignment(Pos.CENTER);
        board = qws.getCurrentDisplayBoard();
        for(int i = 0; i < QuantumWordSearch.getX_BOARD_DIM(); i++) {
            for(int j = 0; j < QuantumWordSearch.getY_BOARD_DIM(); j++) {
                char letter = board[i][j].getLetter();
                grid.add(makeButton(letter), j, i);
            }
        }
        // display word search board
        Scene scene = new Scene(grid);
        stage.setTitle("Quantum Word Search");
        stage.setScene(scene);
        stage.show();
    }

    public Button makeButton(char letter) {
        Button button = new Button();
        button.setPrefSize(40, 40);
        button.setPadding(Insets.EMPTY);
        button.setBorder(new Border(
            new BorderStroke(
                Color.BLACK, 
                BorderStrokeStyle.SOLID, 
                CornerRadii.EMPTY, 
                BorderStroke.THIN)));
        button.setText(String.valueOf(letter));
        button.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
