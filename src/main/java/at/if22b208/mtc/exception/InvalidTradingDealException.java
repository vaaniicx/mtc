package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to an invalid trading deal.
 */
public class InvalidTradingDealException extends Exception {
    /**
     * Constructs a new {@code InvalidTradingDealException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidTradingDealException(String message) {
        super(message);
    }
}
