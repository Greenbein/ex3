package exceptions;

public class ExceedingBoundariesResException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";
    public ExceedingBoundariesResException() {
        super(DEFAULT_MESSAGE);
    }
}
