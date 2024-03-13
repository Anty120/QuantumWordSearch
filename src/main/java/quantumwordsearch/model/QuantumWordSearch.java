package quantumwordsearch.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QuantumWordSearch {
    private static int X_BOARD_DIM = 10;
    private static int Y_BOARD_DIM = 10;
    private Tile[][] firstBoard = new Tile[X_BOARD_DIM][Y_BOARD_DIM];
    private Tile[][] secondBoard = new Tile[X_BOARD_DIM][Y_BOARD_DIM];
    private boolean isOnFirstBoard = true;

    public QuantumWordSearch(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); //skipping the header line
            populateBoard(br.readLine(), firstBoard);
            populateBoard(br.readLine(), secondBoard);
        } catch (FileNotFoundException FNFE) {
            System.out.println("File does not exist. Please check the file path given.");
        }
    }

    private void populateBoard(String line, Tile[][] board) {
        String[] rows = line.split(",");
        for (int i = 0; i < rows.length; i++) { // looping through the array of rows
            String row = rows[i];
            for (int j = 0; j < row.length(); j++) { // looping through each letter in the row
                char letter = row.charAt(j);
                board[i][j] = new Tile(i, j, letter);
            }
        }
    }

    //testing for display to terminal
    public void displayWordSearch() {
        String output = "0 1 2 3 4 5 6 7 8 9 10\n";
        try {
            for (int i = 0; i < X_BOARD_DIM; i++) {
                output += i+1 + " "; //add row numbers (row 1, row 2, ...)             
                for (int j = 0; j < Y_BOARD_DIM - 1; j++) {
                    char letter = firstBoard[i][j].getLetter();
                    output += letter + " ";
                }
                char lastLetter = firstBoard[i][Y_BOARD_DIM-1].getLetter();
                output += lastLetter + "\n";
            }
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Please check your board dimensions or your csv data file for inconsistences with formatting the dimensions of the lines provided.");
        }
    }

    public static void main(String[] args) throws IOException {
        QuantumWordSearch qws = new QuantumWordSearch("data/example.csv");
        qws.displayWordSearch();
    }
}
