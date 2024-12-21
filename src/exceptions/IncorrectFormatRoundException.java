package exceptions;

public class IncorrectFormatRoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Did not change rounding method due to incorrect format.";
    public IncorrectFormatRoundException() {
        super(DEFAULT_MESSAGE);
    }
}
