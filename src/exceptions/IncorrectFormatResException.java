package exceptions;

public class IncorrectFormatResException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Did not change resolution due to incorrect format.";
    public IncorrectFormatResException() {
        super(DEFAULT_MESSAGE);
    }
}
