package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to a negative balance.
 */
public class NegativeBalanceException extends Exception {
    /**
     * Constructs a new {@code NegativeBalanceException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public NegativeBalanceException(String message) {
        super(message);
    }
}
