package flashcards;

public class FlashCard {
    private final String term;
    private final String definition;
    private Integer errorCounter;

    public FlashCard(String term, String definition, int errorCounter) {
        this.term = term;
        this.definition = definition;
        this.errorCounter = errorCounter;
    }

    public void incrementErrorCounter() {
        this.errorCounter++;
    }

    public void resetErrorCounter() {
        this.errorCounter = 0;
    }

    public String getTerm() {
        return this.term;
    }

    public String getDefinition() {
        return this.definition;
    }

    public int getErrorCounter() {
        return this.errorCounter;
    }
}
