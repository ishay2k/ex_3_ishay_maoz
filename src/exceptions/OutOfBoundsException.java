package exceptions;

/**
 * Thrown to indicate that a given value (such as resolution) is outside the allowed boundaries.
 *
 * <p>This exception is typically used when attempting to perform an operation that violates
 * defined constraints, such as increasing or decreasing the image resolution beyond valid limits
 * in the ASCII art application.</p>
 *
 */
public class OutOfBoundsException extends RuntimeException {

    /**
     * Constructs a new OutOfBoundsException with the specified detail message.
     *
     * @param message the detail message explaining the boundary violation
     */
    public OutOfBoundsException(String message) {
        super(message);
    }
}
