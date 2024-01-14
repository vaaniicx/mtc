package at.if22b208.mtc.util.balance;

import java.math.BigInteger;

public class AddOperation implements BalanceOperation {
    @Override
    public BigInteger operate(BigInteger a, BigInteger b) {
        return a.add(b);
    }
}
