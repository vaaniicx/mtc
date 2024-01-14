package at.if22b208.mtc.util.balance;

import at.if22b208.mtc.exception.NegativeBalanceException;

import java.math.BigInteger;

public class SubtractOperation implements BalanceOperation {
    @Override
    public BigInteger operate(BigInteger a, BigInteger b) throws NegativeBalanceException {
        if (a.subtract(b).compareTo(BigInteger.ZERO) < 0) {
            throw new NegativeBalanceException("Transaction would result in a balance less than zero.");
        }
        return a.subtract(b);
    }
}
