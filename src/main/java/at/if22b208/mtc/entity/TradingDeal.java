package at.if22b208.mtc.entity;

import at.if22b208.mtc.entity.enumeration.CardType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.With;

import java.util.UUID;

/**
 * Represents a trading deal, specifying the conditions for trading a card.
 */
@Builder
@ToString
@Data
public class TradingDeal {
    /**
     * The UUID of the trading deal.
     */
    @With
    private UUID uuid;

    /**
     * The UUID of the card to be traded.
     */
    private UUID cardUuid;

    /**
     * The required {@link CardType} to trade the card.
     */
    private CardType cardType;

    /**
     * The minimum damage required to trade the card.
     */
    private double minimumDamage;
}
