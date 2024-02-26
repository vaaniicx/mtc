package at.if22b208.mtc.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code CardService} class provides business logic and services related to the {@link Card} entity.
 *
 * <p> It acts as an intermediary between the {@link CardRepository} and the application controllers,
 * handling operations such as card creation, retrieval, and validation.
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
     * @throws InvalidPackageException If the card already exists.
     */
    @Override
    public Card create(Card card)
            throws InvalidPackageException, DatabaseTransactionException {
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
    public Card getById(UUID uuid)
            throws DatabaseTransactionException {
        return CardRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    /**
     * Retrieves a list of all cards owned by a specific user.
     *
     * @param user The user whose cards need to be retrieved.
     * @return A list of cards owned by the specified user.
     */
    public List<Card> getAllByOwner(User user)
            throws DatabaseTransactionException {
        return CardRepository.getInstance()
                .findByOwner(user)
                .stream()
                .flatMap(Optional::stream)
                .toList();
    }

    /**
     * Gets the next available package ID.
     *
     * @return The next available package ID.
     */
    public int getNextPackageId()
            throws DatabaseTransactionException {
        Integer nextPackageId = CardRepository.getInstance().findNextPackageId();
        if (nextPackageId == null) {
            return 1;
        } else {
            return nextPackageId + 1;
        }
    }

    /**
     * Retrieves a package of cards with the required size (currently set to 5 cards).
     *
     * @return A list of cards representing the package.
     * @throws InvalidPackageException If the package size is not as expected.
     */
    public List<Card> getPackage()
            throws InvalidPackageException, DatabaseTransactionException {
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

    /**
     * Updates the owner of a card to a new user.
     *
     * @param card The card whose owner needs to be updated.
     * @param user The new owner of the card.
     */
    public void updateOwner(Card card, User user)
            throws DatabaseTransactionException {
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
