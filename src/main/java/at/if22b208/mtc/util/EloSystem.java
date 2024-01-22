package at.if22b208.mtc.util;

import at.if22b208.mtc.entity.User;

/**
 * Utility class implementing the Elo rating system for updating player ratings based on match outcomes.
 * The Elo system is a method for calculating the relative skill levels of players in two-player games.
 */
public class EloSystem {
    /**
     * The K-factor determines the sensitivity of the system to rating differences.
     */
    private static final int K_FACTOR = 32;

    /**
     * Calculates the expected outcome for player A in a match against player B using the Elo formula.
     *
     * @param ratingA The Elo rating of player A.
     * @param ratingB The Elo rating of player B.
     * @return The expected outcome for player A.
     */
    private static double calculateExpectedOutcome(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
    }

    /**
     * Updates the ratings for two players based on the actual outcome of a match.
     *
     * @param playerA    The User object representing player A.
     * @param playerB    The User object representing player B.
     * @param playerAWon True if player A won the match, false otherwise.
     */
    public static void updateRatings(User playerA, User playerB, boolean playerAWon) {
        double expectedOutcomeA = calculateExpectedOutcome(playerA.getElo(), playerB.getElo());
        double expectedOutcomeB = calculateExpectedOutcome(playerB.getElo(), playerA.getElo());

        // Update ratings based on the outcome of the match
        int outcomeA = playerAWon ? 1 : 0;
        int outcomeB = playerAWon ? 0 : 1;

        // Calculate new ratings
        int newRatingA = (int) (playerA.getElo() + K_FACTOR * (outcomeA - expectedOutcomeA));
        int newRatingB = (int) (playerB.getElo() + K_FACTOR * (outcomeB - expectedOutcomeB));

        // Update player ratings
        playerA.setElo(newRatingA);
        playerB.setElo(newRatingB);
    }
}
