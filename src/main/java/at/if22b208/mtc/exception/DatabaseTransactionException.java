package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to database transactions.
 */
public class DatabaseTransactionException extends Exception {
    /**
     * Constructs a new {@code DatabaseTransactionException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public DatabaseTransactionException(String message) {
        super(message);
    }
}
