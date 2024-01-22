package at.if22b208.mtc.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a single round in a battle with information about the cards involved.
 */
@Builder
@Data
public class Round {
    /**
     * The number identifying the round in the battle sequence.
     */
    private int number;

    /**
     * The card declared as the winner of the round.
     */
    private Card roundWinnerCard;

    /**
     * The card declared as the loser of the round.
     */
    private Card roundLoserCard;
}