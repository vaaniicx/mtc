package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code CardService} class provides business logic and services related to the {@link Card} entity.
 * It acts as an intermediary between the {@link CardRepository} and the application controllers,
 * handling operations such as card creation, retrieval, and validation.
 * <p>
 * This class follows the singleton pattern with a synchronized instance retrieval method.
 * </p>
 *
 * @author Vanessa Kausl
 * @since 2023-01-01
 */
@Slf4j
public class CardService implements Service<Card, UUID> {
    /**
     * The singleton instance of the {@code CardService}.
     */
    private static CardService INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private CardService() {
    }

    /**
     * Creates a new card in the system, checking if a card with the same UUID already exists.
     *
     * @param card The card entity to be created.
     * @return The created card, or {@code null} if a card with the same UUID already exists.
     */
    @Override
    public Card create(Card card) throws InvalidPackageException {
        if (this.getById(card.getUuid()) != null) {
            throw new InvalidPackageException("Card already exists.");
        }
        return CardRepository.getInstance().create(card);
    }

    /**
     * Retrieves a list of all cards in the system. (Currently not implemented)
     *
     * @return A list of all cards in the system.
     */
    @Override
    public List<Card> getAll() {
        return null;
    }

    /**
     * Retrieves a card by its UUID from the system.
     *
     * @param uuid The UUID of the card to retrieve.
     * @return The card with the specified UUID, or {@code null} if not found.
     */
    @Override
    public Card getById(UUID uuid) {
        return CardRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    public List<Card> getAllByOwner(User user) {
        return CardRepository.getInstance()
                .findByOwner(user)
                .stream()
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Card> getPackage() throws InvalidPackageException {
        List<Card> cards = CardRepository.getInstance().findAvailablePackage()
                .stream()
                .flatMap(Optional::stream)
                .toList();

        if (cards.size() != 5) {
            log.warn("Invalid package size. Expected 5 cards, but found {} cards.", cards.size());
            throw new InvalidPackageException("Invalid package size. Expected 5 cards.");
        }
        return cards;
    }

    public void updateOwner(Card card, User user) {
        CardRepository.getInstance().updateOwner(card, user);
    }

    /**
     * Gets a singleton instance of the {@code CardService}.
     *
     * @return The singleton instance of the {@code CardService}.
     */
    public static synchronized CardService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardService();
        }
        return INSTANCE;
    }
}
