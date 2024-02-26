package at.if22b208.mtc.util.balance;

import java.math.BigInteger;

import at.if22b208.mtc.exception.NegativeBalanceException;

/**
 * The {@code SubtractOperation} class implements the {@link BalanceOperation} interface
 * to perform subtraction operations on {@link BigInteger} instances.
 * It ensures that the result of the subtraction is not negative, throwing an exception if it is.
 */
public class SubtractOperation implements BalanceOperation {
    /**
     * Performs a subtraction operation on two {@link BigInteger} values.
     *
     * @param a The first {@link BigInteger} operand.
     * @param b The second {@link BigInteger} operand.
     * @return The result of the subtraction operation.
     * @throws NegativeBalanceException If the operation results in a negative balance.
     */
    @Override
    public BigInteger operate(BigInteger a, BigInteger b)
            throws NegativeBalanceException {
        if (a.subtract(b).compareTo(BigInteger.ZERO) < 0) {
            throw new NegativeBalanceException("Transaction would result in a balance less than zero.");
        }
        return a.subtract(b);
    }
}
