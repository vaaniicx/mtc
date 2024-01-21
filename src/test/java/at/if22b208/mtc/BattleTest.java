package at.if22b208.mtc;

import at.if22b208.mtc.config.MtcConstants;
import at.if22b208.mtc.entity.Battle;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.entity.enumeration.CardElementType;
import at.if22b208.mtc.entity.enumeration.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {
    private User playerA;
    private User playerB;
    private Battle battle;

    @BeforeEach
    void setUp() {
        // Initialize players and battle before each test
        playerA = User.builder().uuid(UUID.randomUUID()).username("PlayerA").build();
        playerB = User.builder().uuid(UUID.randomUUID()).username("PlayerB").build();

        battle = new Battle();
        battle.setPlayerA(playerA);
        battle.setPlayerB(playerB);
    }

    @Test
    @DisplayName("Test battle with expected to result in a draw")
    void test_battleWithDraw() {
        List<Card> deckA = new ArrayList<>();
        deckA.add(Card.builder().name("TestCard").cardType(CardType.MONSTER).cardElementType(CardElementType.NORMAL).damage(10).userUuid(playerA.getUuid()).build());

        List<Card> deckB = List.copyOf(deckA);

        // Set the decks for both players
        playerA.setDeck(deckA);
        playerB.setDeck(deckB);

        // Perform the battle
        battle.battle();

        // Verify that it's a draw
        assertNull(battle.getWinner());
        assertTrue(battle.isDraw());
        assertEquals(MtcConstants.MAX_ROUNDS_PER_BATTLE, battle.getRounds().size());
    }

    @Test
    @DisplayName("Test battle with cards 'Dragon' vs 'Ork' with expected winner PlayerB")
    void test_battleMonsterFight() {
        preparePlayerDecks("Dragon", "Ork");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerB);
    }

    @Test
    @DisplayName("Test battle with cards 'WaterSpell' vs 'FireSpell' with expected winner PlayerA")
    void test_battleSpellFight() {
        preparePlayerDecks("WaterSpell", "FireSpell");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerA);
    }


    @Test
    @DisplayName("Test battle with cards 'RegularSpell' vs 'WaterGoblin' with expected winner PlayerA")
    void test_battleMixedFight() {
        preparePlayerDecks("RegularSpell", "WaterGoblin");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerA);
    }

    @Test
    @DisplayName("Test battle with cards 'Goblin' vs 'Dragon' with expected winner PlayerB")
    void test_battleSpecialityGoblinVsDragon() {
        preparePlayerDecks("Goblin", "Dragon");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerB);
    }

    @Test
    @DisplayName("Test battle with cards 'Wizard' vs 'Ork' with expected winner PlayerA")
    void test_battleSpecialityWizardVsOrk() {
        preparePlayerDecks("Wizard", "Ork");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerA);
    }

    @Test
    @DisplayName("Test battle with cards 'FireElf' vs 'Dragon' with expected winner PlayerA")
    void test_battleSpecialityFireElfVsDragon() {
        preparePlayerDecks("FireElf", "Dragon");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerA);
    }

    @Test
    @DisplayName("Test battle with cards 'Knight' vs 'WaterSpell' with expected winner playerB")
    void test_battleSpecialityKnightVsWaterSpell() {
        preparePlayerDecks("Knight", "WaterSpell");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerB);
    }

    @Test
    @DisplayName("Test battle with cards 'Kraken' vs 'FireSpell' with expected winner playerA")
    void test_battleSpecialityKrakenVsFireSpell() {
        preparePlayerDecks("Kraken", "FireSpell");
        battle.battle();
        assertionsOnBattleWithExpectedWinner(playerA);
    }

    private void preparePlayerDecks(String cardNameA, String cardNameB) {
        List<Card> deckA = new ArrayList<>();
        deckA.add(Card.builder().name(cardNameA).damage(10).userUuid(playerA.getUuid()).build());

        List<Card> deckB = new ArrayList<>();
        deckB.add(Card.builder().name(cardNameB).damage(20).userUuid(playerB.getUuid()).build());

        playerA.setDeck(deckA);
        playerB.setDeck(deckB);
    }


    private void assertionsOnBattleWithExpectedWinner(User expectedWinner) {
        assertAll(
                () -> assertNotNull(battle.getWinner()),
                () -> assertEquals(expectedWinner, battle.getWinner()),
                () -> assertFalse(battle.isDraw())
        );
    }
}
