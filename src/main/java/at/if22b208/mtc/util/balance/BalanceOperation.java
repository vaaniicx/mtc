package at.if22b208.mtc.util.balance;

import at.if22b208.mtc.exception.BalanceTransactionException;
import at.if22b208.mtc.exception.NegativeBalanceException;

import java.math.BigInteger;

public interface BalanceOperation {
    BigInteger operate(BigInteger a, BigInteger b) throws BalanceTransactionException, NegativeBalanceException;
}
