package at.if22b208.mtc.entity;

import at.if22b208.mtc.config.MtcConstants;
import at.if22b208.mtc.entity.enumeration.AttackEffectiveness;
import at.if22b208.mtc.entity.enumeration.CardElementType;
import at.if22b208.mtc.entity.enumeration.CardType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * Represents a battle between two players with decks of cards.
 * The battle involves a series of rounds, each consisting of card draws,
 * special interactions, and winner determination. The battle continues until
 * a winner is determined or the maximum number of rounds is reached.
 * <p>
 * This class encapsulates the logic for handling various aspects of the battle,
 * including interactions between different types of cards (monsters, spells),
 * determining the winner based on drawn cards, and updating player decks.
 */
@Data
@Slf4j
public class Battle {
    private CountDownLatch latch = new CountDownLatch(2);
    private User playerA;
    private User playerB;
    private User winner;
    private boolean isDraw = false;
    private List<Round> rounds = new ArrayList<>();

    /**
     * Initiates the battle between two players, playing a series of rounds until a winner is determined.
     */
    public void battle() {
        SecureRandom random = new SecureRandom();

        for (int i = 1; i <= MtcConstants.MAX_ROUNDS_PER_BATTLE; i++) {
            Card cardA = drawRandomCard(playerA.getDeck(), random);
            Card cardB = drawRandomCard(playerB.getDeck(), random);

            // Logging information about the cards drawn
            log.info("Round " + i + ": PlayerA card - " + cardA);
            log.info("Round " + i + ": PlayerB card - " + cardB);

            // Determine winner if possible
            determineWinnerIfPossible(cardA, cardB);
            if (winner != null) {
                break; // Stop the loop if the winner is determined
            }

            Round round = createRound(i);

            handleMonsterFight(round, Card.copy(cardA), Card.copy(cardB));
            handleSpellFight(round, Card.copy(cardA), Card.copy(cardB));
            handleMixedFight(round, Card.copy(cardA), Card.copy(cardB));

            this.rounds.add(round);
            overtakeCard(round);

            if (i == MtcConstants.MAX_ROUNDS_PER_BATTLE) {
                // Reached the maximum number of rounds without a winner, consider it a draw
                this.winner = null;
                this.isDraw = true;
            }

            // Logging information about the state of players after each round
            log.info("Round " + i + ": PlayerA deck - " + playerA.getDeck());
            log.info("Round " + i + ": PlayerB deck - " + playerB.getDeck());
        }
    }

    /**
     * Creates a new round with the given round number.
     *
     * @param roundNumber The round number.
     * @return A new Round instance.
     */
    private Round createRound(int roundNumber) {
        return Round.builder()
                .number(roundNumber)
                .build();
    }

    /**
     * Draws a random card from the given deck using the provided SecureRandom instance.
     *
     * @param deck   The deck to draw from.
     * @param random The SecureRandom instance for randomness.
     * @return A randomly selected card or null if the deck is empty.
     */
    private Card drawRandomCard(List<Card> deck, SecureRandom random) {
        return deck.isEmpty() ? null : deck.get(random.nextInt(deck.size()));
    }

    /**
     * Determines the winner based on the availability of cards for both players.
     *
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void determineWinnerIfPossible(Card cardA, Card cardB) {
        if (cardA == null && cardB != null) {
            this.winner = this.playerB;
        } else if (cardB == null && cardA != null) {
            this.winner = this.playerA;
        }
    }

    /**
     * Handles a battle between two monster cards, updating round information.
     *
     * @param round Round information.
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void handleMonsterFight(Round round, Card cardA, Card cardB) {
        if (isMonsterFight(cardA, cardB)) {
            handleSpecialMonsterInteraction(cardA, cardB);
            setWinningAndLosingCardForRound(round, cardA, cardB);
        }
    }

    /**
     * Handles a battle between two spell cards, updating round information.
     *
     * @param round Round information.
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void handleSpellFight(Round round, Card cardA, Card cardB) {
        if (isSpellFight(cardA, cardB)) {
            handleSpellEffects(cardA, cardB);
            setWinningAndLosingCardForRound(round, cardA, cardB);
        }
    }

    /**
     * Handles a battle between a mixed combination of cards, updating round information.
     *
     * @param round Round information.
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void handleMixedFight(Round round, Card cardA, Card cardB) {
        if (!isMonsterFight(cardA, cardB) && !isSpellFight(cardA, cardB)) {
            handleMixedEffects(cardA, cardB);
            setWinningAndLosingCardForRound(round, cardA, cardB);
        }
    }

    /**
     * Handles special interactions between two monster cards, updating their properties accordingly.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleSpecialMonsterInteraction(Card a, Card b) {
        handleGoblinVsDragon(a, b);
        handleWizardVsOrk(a, b);
        handleFireElfVsDragon(a, b);
    }

    /**
     * Handles the effects of a battle between two spell cards, updating round information.
     *
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void handleSpellEffects(Card cardA, Card cardB) {
        calculateAttacksBasedOnEffectiveness(getAttackEffectiveness(cardA, cardB), cardA);
        calculateAttacksBasedOnEffectiveness(getAttackEffectiveness(cardB, cardA), cardB);
    }

    /**
     * Handles the effects of a battle between a mixed combination of cards, updating round information.
     *
     * @param cardA Card from Player A.
     * @param cardB Card from Player B.
     */
    private void handleMixedEffects(Card cardA, Card cardB) {
        calculateAttacksBasedOnEffectiveness(getAttackEffectiveness(cardA, cardB), cardA);
        calculateAttacksBasedOnEffectiveness(getAttackEffectiveness(cardB, cardA), cardB);
        handleKnightVsWaterSpell(cardA, cardB);
        handleKrakenVsSpell(cardA, cardB);
    }

    /**
     * Determines the winner between two monster cards based on special interactions, updating round information.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleGoblinVsDragon(Card a, Card b) {
        if (a.getName().contains("goblin") && b.getName().contains("dragon")) {
            a.setDamage(0);
        } else if (b.getName().contains("goblin") && a.getName().contains("dragon")) {
            b.setDamage(0);
        }
    }

    /**
     * Determines the winner between an ork and a wizard based on special interactions, updating round information.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleWizardVsOrk(Card a, Card b) {
        if (a.getName().contains("ork") && b.getName().contains("wizard")) {
            a.setDamage(0);
        } else if (b.getName().contains("ork") && a.getName().contains("wizard")) {
            b.setDamage(0);
        }
    }

    /**
     * Determines the winner between a knight and a water spell based on special interactions, updating round information.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleKnightVsWaterSpell(Card a, Card b) {
        if (a.getName().contains("knight") && b.getName().equalsIgnoreCase("waterspell")) {
            a.setDamage(0);
        } else if (b.getName().contains("knight") && a.getName().equalsIgnoreCase("waterspell")) {
            b.setDamage(0);
        }
    }

    /**
     * Determines the winner between a spell and a kraken based on special interactions, updating round information.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleKrakenVsSpell(Card a, Card b) {
        if (a.getCardType().equals(CardType.SPELL) && b.getName().contains("kraken")) {
            a.setDamage(0);
        } else if (b.getCardType().equals(CardType.SPELL) && a.getName().contains("kraken")) {
            b.setDamage(0);
        }
    }

    /**
     * Determines the winner between a dragon and a fire elf based on special interactions, updating round information.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     */
    private void handleFireElfVsDragon(Card a, Card b) {
        if (a.getName().contains("dragon") && b.getName().equalsIgnoreCase("fireelf")) {
            a.setDamage(0);
        } else if (b.getName().contains("dragon") && a.getName().equalsIgnoreCase("fireelf")) {
            b.setDamage(0);
        }
    }

    /**
     * Calculates attacks based on effectiveness and updates the card's damage accordingly.
     *
     * @param effectiveness The effectiveness of the attack.
     * @param card          The card to be affected.
     */
    private static void calculateAttacksBasedOnEffectiveness(AttackEffectiveness effectiveness, Card card) {
        switch (effectiveness) {
            case EFFECTIVE -> card.setDamage(card.getDamage() * 2);
            case NOT_EFFECTIVE -> card.setDamage(card.getDamage() / 2);
        }
    }

    /**
     * Checks if a battle is between two monster cards.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     * @return True if it's a battle between two monster cards, false otherwise.
     */
    private boolean isMonsterFight(Card a, Card b) {
        return a.getCardType() == CardType.MONSTER && b.getCardType() == CardType.MONSTER;
    }

    /**
     * Checks if a battle is between two spell cards.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     * @return True if it's a battle between two spell cards, false otherwise.
     */
    private boolean isSpellFight(Card a, Card b) {
        return a.getCardType() == CardType.SPELL && b.getCardType() == CardType.SPELL;
    }

    /**
     * Gets the effectiveness of an attack based on the elemental types of the cards involved.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     * @return The effectiveness of the attack.
     */
    private AttackEffectiveness getAttackEffectiveness(Card a, Card b) {
        CardElementType elementTypeA = a.getCardElementType();
        CardElementType elementTypeB = b.getCardElementType();

        if (elementTypeA.equals(elementTypeB)) {
            return AttackEffectiveness.NO_EFFECT;
        }

        if (elementTypeA == CardElementType.WATER && elementTypeB == CardElementType.FIRE ||
                elementTypeA == CardElementType.FIRE && elementTypeB == CardElementType.NORMAL ||
                elementTypeA == CardElementType.NORMAL && elementTypeB == CardElementType.WATER) {
            return AttackEffectiveness.EFFECTIVE;
        } else {
            return AttackEffectiveness.NOT_EFFECTIVE;
        }
    }

    /**
     * Chooses the winner card based on damage. Returns null in case of a draw.
     *
     * @param a Card from Player A.
     * @param b Card from Player B.
     * @return The winner card or null in case of a draw.
     */
    private void setWinningAndLosingCardForRound(Round round, Card a, Card b) {
        if (a.getDamage() > b.getDamage()) {
            round.setRoundWinnerCard(a);
            round.setRoundLoserCard(b);
        } else if (a.getDamage() < b.getDamage()) {
            round.setRoundWinnerCard(b);
            round.setRoundLoserCard(a);
        } else {
            round.setRoundWinnerCard(null);
            round.setRoundLoserCard(null);
        }
    }

    /**
     * Overtakes the card of the round winner if applicable.
     *
     * @param round The current round.
     */
    private void overtakeCard(Round round) {
        Card roundWinnerCard = round.getRoundWinnerCard();
        Card roundLoserCard = round.getRoundLoserCard();

        if (roundWinnerCard != null && roundLoserCard != null) {
            User currentWinner = roundWinnerCard.getUserUuid().equals(playerA.getUuid()) ? playerA : playerB;
            User currentLoser = roundLoserCard.getUserUuid().equals(playerB.getUuid()) ? playerB : playerA;

            UUID loserCardUuid = roundLoserCard.getUuid();
            int indexOfLoserCard = IntStream.range(0, currentLoser.getDeck().size())
                    .filter(index -> currentLoser.getDeck().get(index).getUuid().equals(loserCardUuid))
                    .findFirst()
                    .orElse(-1);
            if (indexOfLoserCard != -1) {
                List<Card> loserDeck = new ArrayList<>(currentLoser.getDeck());
                Card cardToOvertake = loserDeck.remove(indexOfLoserCard);
                currentLoser.setDeck(loserDeck);

                List<Card> winnerDeck = new ArrayList<>(currentWinner.getDeck());
                winnerDeck.add(cardToOvertake);
                currentWinner.setDeck(winnerDeck);
            }
        }
    }
}