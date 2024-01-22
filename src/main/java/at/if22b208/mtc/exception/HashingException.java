package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to hashing operations.
 */
public class HashingException extends Exception {
    /**
     * Constructs a new {@code HashingException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
