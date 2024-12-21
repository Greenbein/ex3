package exceptions;

public class IncorrectFormatAddException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Did not add due to incorrect format.";

    public IncorrectFormatAddException() {
       super(DEFAULT_MESSAGE);
    }
}
