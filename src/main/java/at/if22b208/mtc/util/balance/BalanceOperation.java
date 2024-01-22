package at.if22b208.mtc.util.balance;

import at.if22b208.mtc.exception.NegativeBalanceException;

import java.math.BigInteger;

/**
 * The {@code BalanceOperation} interface defines the contract for balance operations on {@link BigInteger} instances.
 * Implementing classes should provide the logic to perform a specific balance operation, such as addition or subtraction.
 */
public interface BalanceOperation {
    /**
     * Performs a balance operation on two {@link BigInteger} values.
     *
     * @param a The first {@link BigInteger} operand.
     * @param b The second {@link BigInteger} operand.
     * @return The result of the balance operation.
     * @throws NegativeBalanceException If the operation results in a negative balance.
     */
    BigInteger operate(BigInteger a, BigInteger b) throws NegativeBalanceException;
}
