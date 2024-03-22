package quantumwordsearch.view;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import quantumwordsearch.model.QuantumWordSearch;
import quantumwordsearch.model.Tile;

public class QuantumWordSearchGUI extends Application {
    private QuantumWordSearch qws;
    private GridPane wordGrid = new GridPane();
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
        wordGrid.setAlignment(Pos.CENTER);
        board = qws.getCurrentDisplayBoard();
        for(int i = 0; i < QuantumWordSearch.getX_BOARD_DIM(); i++) {
            for(int j = 0; j < QuantumWordSearch.getY_BOARD_DIM(); j++) {
                char letter = board[i][j].getLetter();
                Button button = makeButton(letter);
                wordGrid.add(button, j, i);
            }
        }
        // make word list for the game
        wordList.setHgap(20);
        wordList.setVgap(0);
        wordList.setPadding(new Insets(10, 10, 10, 10));
        wordList.setAlignment(Pos.CENTER);
        wordList.setMaxSize(400, 100);
        
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints(); // Set column constraints to evenly distribute the columns
            column.setPercentWidth(100 / 3); // Evenly distribute 3 columns
            wordList.getColumnConstraints().add(column);
        }

        Label titleLabel = new Label("WORD LIST");
        titleLabel.setFont(Font.font(20));
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        wordList.add(titleLabel, 0, 0, 3, 1); 

        ArrayList<String> words = qws.getWordList();
        int rowIndex = 1; // first row (index 0) occupied by the titleLabel
        for (int i = 0; i < words.size(); i += 3) {
            for (int j = 0; j < 3 && i + j < words.size(); j++) {
                Label word = new Label(words.get(i + j));
                word.setFont(Font.font(20));
                GridPane.setHalignment(word, HPos.CENTER);
                wordList.add(word, j, rowIndex);
            }
            rowIndex++;
        }

        ScrollPane scrollPane = new ScrollPane(wordList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setBorder(new Border(
            new BorderStroke(
                Color.BLUE, 
                BorderStrokeStyle.DASHED, 
                CornerRadii.EMPTY, 
                BorderStroke.THIN)));
        // make a VBox to store the word grid and word list and extras
        VBox vbox = new VBox();
        vbox.getChildren().add(wordGrid);
        vbox.getChildren().add(scrollPane);
        // display word search board
        Scene scene = new Scene(vbox);
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
        button.setStyle("-fx-background-color: #FFFFFF;");
        button.setText(String.valueOf(letter));
        button.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
