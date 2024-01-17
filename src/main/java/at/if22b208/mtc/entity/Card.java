/**
 * The {@code Card} class represents a card entity with attributes such as UUID, name, damage, package UUID, and user UUID.
 *
 * @author Vanessa Kausl
 * @version 1.0
 * @since 14.01.2023
 */
package at.if22b208.mtc.entity;

import at.if22b208.mtc.entity.enumeration.CardName;
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
     * See also {@link CardName}
     */
    private String name;

    /**
     * The damage value associated with the card.
     */
    private int damage;

    /**
     * The UUID of the package to which the card belongs.
     */
    private UUID packageUuid;

    /**
     * The UUID of the user to whom the card is associated.
     */
    private UUID userUuid;
}
