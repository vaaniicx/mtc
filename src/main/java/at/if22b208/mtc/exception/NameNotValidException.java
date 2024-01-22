package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to an invalid name.
 */
public class NameNotValidException extends Exception {
    /**
     * Constructs a new {@code NameNotValidException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public NameNotValidException(String message) {
        super(message);
    }
}
