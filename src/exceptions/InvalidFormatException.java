package exceptions;

/**
 * Thrown to indicate that a user command or input has an invalid format.
 *
 * <p>This exception is typically used in scenarios where user input does not
 * conform to the expected format, such as malformed commands or unsupported
 * syntax within the ASCII art shell interface.</p>
 *
 */
public class InvalidFormatException extends Exception {

    /**
     * Constructs a new InvalidFormatException with the specified detail message.
     *
     * @param message the detail message explaining the format violation
     */
    public InvalidFormatException(String message) {
        super(message);
    }
}
