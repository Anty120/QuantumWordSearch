package quantumwordsearch.view;

import java.io.IOException;
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
import quantumwordsearch.model.Word;

public class QuantumWordSearchGUI extends Application {
    private QuantumWordSearch qws;
    private GridPane wordGrid = new GridPane();
    private GridPane wordList = new GridPane();
    private Button[][] buttons = new Button[QuantumWordSearch.getX_BOARD_DIM()][QuantumWordSearch.getY_BOARD_DIM()];
    private Label[] labels;

    @Override
    public void start(Stage stage) throws Exception {
        initializeGameFromFile();
        makeWordGrid();
        makeWordList();
        // make a button to toggle the board
        Button toggleButton = new Button("Toggle Board");
        toggleButton.setOnAction(e -> {
            qws.toggleBoard();
            updateGrid();
        });
        // make a Vbox to store the word grid and word list
        VBox vbox = new VBox();
        vbox.getChildren().add(wordGrid);
        vbox.getChildren().add(wordList);
        vbox.getChildren().add(toggleButton);
        // display word search board
        Scene scene = new Scene(vbox);
        stage.setTitle("Quantum Word Search");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeGameFromFile() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        String filename = scanner.nextLine();
        scanner.close();
        qws = new QuantumWordSearch(filename);
    }

    public void makeWordGrid() {
        wordGrid.setAlignment(Pos.CENTER);
        Tile[][] board = qws.getCurrentBoard();
        for (int i = 0; i < QuantumWordSearch.getX_BOARD_DIM(); i++) {
            for (int j = 0; j < QuantumWordSearch.getY_BOARD_DIM(); j++) {
                char letter = board[i][j].getLetter();
                Button button = makeButton(i, j, letter);
                buttons[i][j] = button;
                wordGrid.add(button, j, i);
            }
        }
    }

    public void makeWordList() {
        wordList.setHgap(20);
        wordList.setVgap(0);
        wordList.setPadding(new Insets(10, 10, 10, 10));
        wordList.setAlignment(Pos.CENTER);
        wordList.setMaxSize(400, 100);
        wordList.setStyle("-fx-background-color: #FFFFFF;");
        // set column constraints to evenly distribute the columns
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 3); // Evenly distribute 3 columns
            wordList.getColumnConstraints().add(column);
        }
        // add title label
        Label titleLabel = new Label("WORD LIST");
        titleLabel.setFont(Font.font(20));
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        wordList.add(titleLabel, 0, 0, 3, 1);
        // add the actual words to the wordList
        ArrayList<Word> words = qws.getWordList();
        labels = new Label[words.size()];
        int rowIndex = 1; // first row (index 0) occupied by the titleLabel
        for (int i = 0; i < words.size(); i += 3) {
            for (int j = 0; j < 3 && i + j < words.size(); j++) {
                Word word = words.get(i + j);
                Label label = new Label(word.getWord());
                label.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
                labels[i + j] = label;
                GridPane.setHalignment(label, HPos.CENTER);
                wordList.add(label, j, rowIndex);
            }
            rowIndex++;
        }
        // making scrollpane so that we can fit lots of words if needed
        ScrollPane scrollPane = new ScrollPane(wordList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefWidth(100);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public Button makeButton(int row, int col, char letter) {
        Button button = new Button();
        button.setPrefSize(40, 40);
        button.setPadding(Insets.EMPTY);
        button.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
        button.setStyle("-fx-background-color: #FFFFFF;");
        button.setText(String.valueOf(letter));
        button.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        button.setOnAction(e -> {
            boolean success = qws.selectTile(row, col);
            if (success) {
                updateGrid();
                updateWordList();
            }
        });
        return button;
    }

    public void updateGrid() {
        Tile[][] board = qws.getCurrentBoard();
        for (int i = 0; i < QuantumWordSearch.getX_BOARD_DIM(); i++) {
            for (int j = 0; j < QuantumWordSearch.getY_BOARD_DIM(); j++) {
                Tile tile = board[i][j];
                Button button = buttons[i][j];
                String letter = String.valueOf(tile.getLetter());
                button.setText(letter);
            }
        }
        for (Tile t : qws.getSelectedTiles()) {
            Button button = buttons[t.getRow()][t.getCol()];
            button.setStyle("-fx-background-color: #87CEEB;");
        }
        for (Tile t : qws.getCorrectTiles()) {
            Button button = buttons[t.getRow()][t.getCol()];
            button.setStyle("-fx-background-color: #00FF00;");
        }
    }

    public void updateWordList() {
        ArrayList<Word> words = qws.getWordList();
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            if (word.isSolved()) {
                Label label = labels[i];
                label.setTextFill(Color.LIMEGREEN);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
