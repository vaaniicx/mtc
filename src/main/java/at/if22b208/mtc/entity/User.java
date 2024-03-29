/**
 * The {@code User} class represents a user entity with attributes such as UUID, username, password, balance, stack, and deck.
 *
 * @author Vanessa Kausl
 * @version 1.0
 * @since 14.01.2023
 */
package at.if22b208.mtc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

/**
 * A builder-pattern based class for creating instances of {@code User}.
 */
@Builder
@Data
public class User {
    /**
     * The UUID of the user.
     */
    @With
    private UUID uuid;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password associated with the user.
     */
    private String password;

    /**
     * The balance of the user, represented as a {@code BigInteger}.
     */
    private BigInteger balance;

    /**
     * The list of cards in the user's stack.
     */
    private List<Card> stack;

    /**
     * The list of cards in the user's deck.
     */
    private List<Card> deck;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The biography of the user.
     */
    private String biography;

    /**
     * The image of the user as a String.
     */
    private String image;

    /**
     * The elo of the user.
     */
    private int elo;

    /**
     * The number of wins of the user.
     */
    private int wins;

    /**
     * The number of losses of the user.
     */
    private int losses;
}
