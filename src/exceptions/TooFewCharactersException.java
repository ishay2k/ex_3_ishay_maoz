package exceptions;

public class TooFewCharactersException extends RuntimeException {
    public TooFewCharactersException(String message) {
        super(message);
    }
}
