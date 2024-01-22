package at.if22b208.mtc.util;

import at.if22b208.mtc.entity.User;

public class EloSystem {
    // K-factor determines the sensitivity of the system to rating differences
    private static final int K_FACTOR = 32;

    // Calculate the expected outcome for player A in a match against player B
    private static double calculateExpectedOutcome(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
    }

    // Update the ratings for two players based on the actual outcome of a match
    public static void updateRatings(User playerA, User playerB, boolean playerAWon) {
        double expectedOutcomeA = calculateExpectedOutcome(playerA.getElo(), playerB.getElo());
        double expectedOutcomeB = calculateExpectedOutcome(playerB.getElo(), playerA.getElo());

        // Update ratings based on the outcome of the match
        int outcomeA = playerAWon ? 1 : 0;
        int outcomeB = playerAWon ? 0 : 1;


        int newRatingA = (int) (playerA.getElo() + K_FACTOR * (outcomeA - expectedOutcomeA));
        int newRatingB = (int) (playerB.getElo() + K_FACTOR * (outcomeB - expectedOutcomeB));

        // Update player ratings
        playerA.setElo(newRatingA);
        playerB.setElo(newRatingB);
    }
}
