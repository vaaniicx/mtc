package at.if22b208.mtc.util.balance;

import java.math.BigInteger;

/**
 * The {@code AddOperation} class represents an addition operation for {@link BigInteger} instances.
 * It implements the {@link BalanceOperation} interface, providing the logic to add two {@link BigInteger} values.
 */
public class AddOperation implements BalanceOperation {
    /**
     * Performs the addition operation on two {@link BigInteger} values.
     *
     * @param a The first {@link BigInteger} operand.
     * @param b The second {@link BigInteger} operand.
     * @return The result of adding {@code a} and {@code b}.
     */
    @Override
    public BigInteger operate(BigInteger a, BigInteger b) {
        return a.add(b);
    }
}
