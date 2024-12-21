package exceptions;

public class InccorectCommandException extends RuntimeException {
  private static final String DEFAULT_MESSAGE =
          "Did not execute due to incorrect command.";
  public InccorectCommandException() {
    super(DEFAULT_MESSAGE);
  }
}
