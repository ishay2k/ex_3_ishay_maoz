package exceptions;

/**
 * Thrown to indicate that an operation requiring a minimum number of characters
 * was attempted with too few characters in the character set.
 *
 * <p>This exception is typically used when the ASCII art algorithm is run
 * with fewer than the required number of characters (e.g., less than 2),
 * which would make brightness mapping unreliable or impossible.</p>
 *
 */
public class TooFewCharactersException extends RuntimeException {

    /**
     * Constructs a new TooFewCharactersException with the specified detail message.
     *
     * @param message the detail message explaining the character count violation
     */
    public TooFewCharactersException(String message) {
        super(message);
    }
}

