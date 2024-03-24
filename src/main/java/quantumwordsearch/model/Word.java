package quantumwordsearch.model;

public class Word {
    private String word;
    private boolean solved;

    public Word(String word) {
        this.word = word;
        this.solved = false;
    }

    public String getWord() {
        return word;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
