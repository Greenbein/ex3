package exceptions;

public class IncorrectFormatOutputException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Did not change output method due to incorrect format.";
    public IncorrectFormatOutputException() {
        super(DEFAULT_MESSAGE);
    }
}
