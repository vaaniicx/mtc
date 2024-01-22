/**
 * The {@code Card} class represents a card entity with attributes such as UUID, name, damage, package UUID, and user UUID.
 *
 * @author Vanessa Kausl
 * @version 1.0
 * @since 14.01.2023
 */
package at.if22b208.mtc.entity;

import at.if22b208.mtc.entity.enumeration.CardElementType;
import at.if22b208.mtc.entity.enumeration.CardType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.With;

import java.util.UUID;

/**
 * A builder-pattern based class for creating instances of {@code Card}.
 */
@Builder
@ToString
@Data
public class Card {
    /**
     * The UUID of the card.
     */
    @With
    private UUID uuid;

    /**
     * The name of the card.
     */
    private String name;

    /**
     * The damage value associated with the card.
     */
    private double damage;

    /**
     * The ID of the package to which the card belongs.
     */
    private int packageId;

    /**
     * The UUID of the user to whom the card is associated.
     */
    private UUID userUuid;

    /**
     * The type of the card (e.g., Spell or Monster).
     */
    private CardType cardType;

    /**
     * The element type of the card (e.g., Fire, Water, or Normal).
     */
    private CardElementType cardElementType;

    /**
     * Copy constructor to create a new instance with the same values as an existing card.
     *
     * @param originalCard The original card to be copied.
     * @return A new instance of {@code Card} with the same values as the original card.
     */
    public static Card copy(Card originalCard) {
        return Card.builder()
                .uuid(originalCard.getUuid())
                .name(originalCard.getName())
                .damage(originalCard.getDamage())
                .packageId(originalCard.getPackageId())
                .userUuid(originalCard.getUserUuid())
                .cardType(originalCard.getCardType())
                .cardElementType(originalCard.getCardElementType())
                .build();
    }

    /**
     * Builder class for {@code Card}, providing a name transformation based on certain keywords.
     */
    public static class CardBuilder {
        /**
         * Sets the name of the card and determines its type and element based on keywords.
         *
         * @param name The name of the card.
         * @return The modified builder instance.
         */
        public CardBuilder name(String name) {
            this.name = name.toLowerCase();
            this.cardType = this.name.contains("spell") ? CardType.SPELL : CardType.MONSTER;
            this.cardElementType = this.name.contains("fire") ? CardElementType.FIRE : this.name.contains("water") ?
                    CardElementType.WATER : CardElementType.NORMAL;
            return this;
        }
    }
}
