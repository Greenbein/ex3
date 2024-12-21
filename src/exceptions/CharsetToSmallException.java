package exceptions;

public class CharsetToSmallException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Did not execute. Charset is too small.";
    public CharsetToSmallException() {
        super(DEFAULT_MESSAGE);
    }
}
