package app.exceptions;

public class FailToMove extends RuntimeException {
    public FailToMove(String message) {
        super("Failed to move file to new destination");
    }
}
