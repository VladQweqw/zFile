package app.exceptions;

public class NotDirectory extends RuntimeException {
    public NotDirectory(String message) {
        super(message);
    }

    public NotDirectory() {
        super("File selected is not a Folder");
    }
}
