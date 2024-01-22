package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to balance transactions.
 */
public class BalanceTransactionException extends Exception {
    /**
     * Constructs a new {@code BalanceTransactionException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public BalanceTransactionException(String message) {
        super(message);
    }
}
