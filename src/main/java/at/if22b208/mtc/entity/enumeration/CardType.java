/**
 * The {@code CardType} enum represents the possible types of cards, such as MONSTER and SPELL.
 *
 * @author Vanessa Kausl
 * @version 1.0
 * @since 14.01.2023
 */
package at.if22b208.mtc.entity.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration defining different types of cards.
 */
public enum CardType {
    /**
     * Represents a monster card type.
     */
    MONSTER("monster"),

    /**
     * Represents a spell card type.
     */
    SPELL("spell");

    private final String value;

    CardType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static CardType fromValue(String value) {
        for (CardType cardType : values()) {
            if (cardType.value.equalsIgnoreCase(value)) {
                return cardType;
            }
        }
        throw new IllegalArgumentException("Invalid CardType value: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
