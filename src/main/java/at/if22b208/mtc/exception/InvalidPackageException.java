package at.if22b208.mtc.exception;

/**
 * Exception thrown to indicate an error related to an invalid card package.
 */
public class InvalidPackageException extends Exception {
    /**
     * Constructs a new {@code InvalidPackageException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InvalidPackageException(String message) {
        super(message);
    }
}
