package at.if22b208.mtc.config;

import java.math.BigInteger;

public class MtcConstants {
    private MtcConstants() {
        // Private constructor to ensure singleton pattern.
    }

    public static final BigInteger PACKAGE_COST = BigInteger.valueOf(5L);

    public static final int MAX_ROUNDS_PER_BATTLE = 100;
}
