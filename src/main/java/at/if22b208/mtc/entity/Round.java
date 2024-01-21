package at.if22b208.mtc.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a single round in a battle with information about the cards involved.
 */
@Builder
@Data
public class Round {
    private int number;
    private Card roundWinnerCard;
    private Card roundLoserCard;
}