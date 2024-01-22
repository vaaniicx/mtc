package at.if22b208.mtc.util.balance;

import at.if22b208.mtc.exception.NegativeBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link BalanceOperation} implementations.
 * These tests cover the functionality of subtract and add operations.
 */
class BalanceOperationTest {

    /**
     * Tests the subtraction operation of {@link SubtractOperation}.
     *
     * @throws NegativeBalanceException if a negative balance occurs during the operation.
     */
    @Test
    @DisplayName("Performing subtract operation")
    void test_subtract() throws NegativeBalanceException {
        BalanceOperation operation = new SubtractOperation();

        BigInteger result = operation.operate(BigInteger.TWO, BigInteger.ONE);

        assertEquals(BigInteger.ONE, result);
    }

    /**
     * Tests the addition operation of {@link AddOperation}.
     *
     * @throws NegativeBalanceException if a negative balance occurs during the operation.
     */
    @Test
    @DisplayName("Performing add operation")
    void test_add() throws NegativeBalanceException {
        BalanceOperation operation = new AddOperation();

        BigInteger result = operation.operate(BigInteger.ONE, BigInteger.ONE);

        assertEquals(BigInteger.TWO, result);
    }
}
