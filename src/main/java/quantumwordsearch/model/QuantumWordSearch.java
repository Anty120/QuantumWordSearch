package quantumwordsearch.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class QuantumWordSearch {
    private static int X_BOARD_DIM = 10;
    private static int Y_BOARD_DIM = 10;
    private Tile[][] firstBoard = new Tile[X_BOARD_DIM][Y_BOARD_DIM];
    private Tile[][] secondBoard = new Tile[X_BOARD_DIM][Y_BOARD_DIM];
    private boolean isOnFirstBoard = true;
    private ArrayList<Tile> selectedTiles = new ArrayList<>();
    private ArrayList<Tile> correctTiles = new ArrayList<>();
    private ArrayList<String> wordList = new ArrayList<>();

    public QuantumWordSearch(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // skipping the header line for the rows
            populateBoard(br.readLine(), firstBoard);
            populateBoard(br.readLine(), secondBoard);
            br.readLine(); // skipping the header line for the answer key words
            populateAnswers(br.readLine());
        } catch (FileNotFoundException FNFE) {
            System.out.println("File does not exist. Please check the file path given.");
        }
    }

    private void populateBoard(String line, Tile[][] board) {
        String[] rows = line.split(",");
        IntStream.range(0, rows.length)
                .forEach(i -> IntStream.range(0, rows[i].length())
                        .forEach(j -> board[i][j] = new Tile(i, j, rows[i].charAt(j))));
    }

    private void populateAnswers(String line) {
        String words[] = line.split(",");
        for (String word : words) {
            wordList.add(word);
        }
    }

    public boolean selectTile(Tile tile) {
        if (isValidMove(tile)) {
            selectedTiles.add(tile);

            if (isValidWord()) {
                correctTiles.addAll(selectedTiles);
                selectedTiles.clear();
            }
            return true;
        }
        return false;
    }

    private boolean isValidWord() {
        String word = "";
        for(Tile tile : selectedTiles) {
            word += tile.getLetter();
        }
        if(wordList.contains(word)) {
            int index  = wordList.indexOf(word);
            wordList.set(index, "\\u2713"+word); //adds checkmark to show that you completed this word
            return true;
        }
        return false;
    }

    public boolean isValidMove(Tile tile) {
        if (selectedTiles.contains(tile)) {
            return false; // Tile already selected
        }
        if (selectedTiles.size() == 1) {
            return true; // Tile can always form a straight line when there is only one other tile
        }
        if (!selectedTiles.isEmpty()) {
            // Determine the direction of the existing line formed by the selected tiles
            boolean isHorizontal = selectedTiles.stream().allMatch(t -> t.getRow() == selectedTiles.get(0).getRow());
            boolean isVertical = selectedTiles.stream().allMatch(t -> t.getCol() == selectedTiles.get(0).getCol());
            boolean isDiagonal = selectedTiles.stream()
                    .allMatch(t -> Math.abs(t.getRow() - selectedTiles.get(0).getRow()) == Math
                            .abs(t.getCol() - selectedTiles.get(0).getCol()));

            // Check if the new tile aligns with the direction of the existing line
            if (isHorizontal && tile.getRow() != selectedTiles.get(0).getRow()) {
                return false; // Not in the same row
            } else if (isVertical && tile.getCol() != selectedTiles.get(0).getCol()) {
                return false; // Not in the same column
            } else if (isDiagonal && (Math.abs(tile.getRow() - selectedTiles.get(0).getRow()) != Math
                    .abs(tile.getCol() - selectedTiles.get(0).getCol()))) {
                return false; // Not in the same diagonal direction
            }
        }

        return true;
    }

    public void displayWordSearch() {
        StringBuilder boardDisplay = new StringBuilder("");
        // adding the column numbers
        for (int col = 0; col <= Y_BOARD_DIM; col++) {
            boardDisplay.append(col).append(" ");
        }
        boardDisplay.append("\n");
        // adding the word search board
        for (int row = 0; row < X_BOARD_DIM; row++) {
            boardDisplay.append(row + 1).append(" ");
            for (int col = 0; col < Y_BOARD_DIM; col++) {
                boardDisplay.append(firstBoard[row][col].getLetter());
                if (col < Y_BOARD_DIM - 1) {
                    boardDisplay.append(" ");
                }
            }
            boardDisplay.append("\n");
        }
        // adding wordlist
        boardDisplay.append("\n");
        boardDisplay.append("WORD LIST:\n");
        IntStream.range(0, wordList.size())
            .forEach(i -> {
                boardDisplay.append(String.format("%-10s", wordList.get(i)));
                if (i % 2 == 1) {
                    boardDisplay.append("\n");
                }
            }
        );
        // adding the chosen letters to a string for user reference
        boardDisplay.append("\n");
        boardDisplay.append("Selected Letters: ");
        for (Tile tile : selectedTiles) {
            boardDisplay.append(tile.getLetter());
        }

        System.out.println(boardDisplay.toString());
    }

    public static void main(String[] args) throws IOException {
        QuantumWordSearch qws = new QuantumWordSearch("data/example.csv");
        qws.selectTile(qws.firstBoard[0][0]);
        qws.selectTile(qws.firstBoard[1][0]);
        qws.selectTile(qws.firstBoard[0][1]);
        qws.selectTile(qws.firstBoard[2][0]);
        qws.displayWordSearch();
    }
}
