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
        for (Tile tile : selectedTiles) {
            word += tile.getLetter();
        }
        if (wordList.contains(word)) {
            int index = wordList.indexOf(word);
            wordList.set(index, "+" + word); // adds checkmark to show that you completed this word
            return true;
        }
        return false;
    }

    public boolean isValidMove(Tile tile) {
        // Square is already being used
        for (Tile t : selectedTiles) {
            if (t.getRow() == tile.getRow() && t.getCol() == tile.getCol()) {
                return false;
            }
        }
        // Check if the new tile is adjacent to the last tile selected
        if (!selectedTiles.isEmpty()) {
            Tile lastTile = selectedTiles.get(selectedTiles.size() - 1);
            if (Math.abs(tile.getRow() - lastTile.getRow()) > 1 || Math.abs(tile.getCol() - lastTile.getCol()) > 1) {
                return false;
            }
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
                return false;
            } else if (isVertical && tile.getCol() != selectedTiles.get(0).getCol()) {
                return false;
            } else if (isDiagonal && (Math.abs(tile.getRow() - selectedTiles.get(0).getRow()) != Math
                    .abs(tile.getCol() - selectedTiles.get(0).getCol()))) {
                return false;
            }
        }
        return true;
    }

    public void toggleBoard() {
        isOnFirstBoard = !isOnFirstBoard;
    }

    public Tile[][] getCurrentDisplayBoard() {
        Tile[][] currentDisplayBoard = new Tile[X_BOARD_DIM][Y_BOARD_DIM];
        for (int i = 0; i < X_BOARD_DIM; i++) {
            System.arraycopy(isOnFirstBoard ? firstBoard[i] : secondBoard[i], 0, currentDisplayBoard[i], 0, Y_BOARD_DIM);
        }
        selectedTiles.forEach(t -> currentDisplayBoard[t.getRow()][t.getCol()] = t);
        correctTiles.forEach(t -> currentDisplayBoard[t.getRow()][t.getCol()] = t);
        return currentDisplayBoard;
    }

    public void displayWordSearch() {
        StringBuilder boardDisplay = new StringBuilder("");
        // create a temporary board
        Tile[][] currentDisplayBoard = getCurrentDisplayBoard();
        // adding the column numbers
        for (int col = 0; col <= Y_BOARD_DIM; col++) {
            boardDisplay.append(col).append(" ");
        }
        boardDisplay.append("\n");
        // adding the word search board
        for (int row = 0; row < X_BOARD_DIM; row++) {
            boardDisplay.append(row + 1).append(" ");
            for (int col = 0; col < Y_BOARD_DIM; col++) {
                boardDisplay.append(currentDisplayBoard[row][col].getLetter());
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
                });
        // adding the chosen letters to a string for user reference
        boardDisplay.append("\n");
        boardDisplay.append("Selected Letters: ");
        for (Tile tile : selectedTiles) {
            boardDisplay.append(tile.getLetter());
        }
        System.out.println(boardDisplay.toString());
    }

    public static int getX_BOARD_DIM() {
        return X_BOARD_DIM;
    }

    public static int getY_BOARD_DIM() {
        return Y_BOARD_DIM;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public static void main(String[] args) throws IOException {
        QuantumWordSearch qws = new QuantumWordSearch("data/example.csv");
        qws.selectTile(qws.firstBoard[0][0]);
        qws.selectTile(qws.firstBoard[0][1]);
        qws.selectTile(qws.firstBoard[0][2]);
        qws.selectTile(qws.firstBoard[0][3]);
        qws.selectTile(qws.secondBoard[0][4]);
        qws.selectTile(qws.firstBoard[0][6]);
        qws.selectTile(qws.secondBoard[0][5]);
        qws.selectTile(qws.secondBoard[0][6]);
        qws.selectTile(qws.secondBoard[0][7]);
        qws.toggleBoard();
        qws.displayWordSearch();
    }
}
