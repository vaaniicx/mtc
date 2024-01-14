package at.if22b208.mtc.exception;

public class NegativeBalanceException extends Exception {
    public NegativeBalanceException(String message) {
        super(message);
    }
}
