package exceptions;

public class IncorrectFormatRemoveException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Did not remove due to incorrect format.";
    public IncorrectFormatRemoveException() {
        super(DEFAULT_MESSAGE);
    }
}
