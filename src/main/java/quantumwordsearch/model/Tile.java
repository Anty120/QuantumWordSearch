package quantumwordsearch.model;

public class Tile {
    private int row;
    private int col;
    private char letter;

    public Tile(int row, int col, char letter) {
        this.row = row;
        this.col = col;
        this.letter = letter;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getLetter() {
        return letter;
    }
}
