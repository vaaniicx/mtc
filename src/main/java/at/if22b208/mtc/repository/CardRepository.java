package at.if22b208.mtc.repository;

import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.database.Result;
import at.if22b208.mtc.database.Row;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A repository class responsible for handling database operations related to cards.
 *
 * <p>This repository provides methods for finding, creating, and building Card entities from the database.</p>
 *
 * @see Repository
 * @see Card
 */
public class CardRepository implements Repository<Card, UUID> {
    private static CardRepository INSTANCE;
    private static final String TABLE = "card";

    /**
     * Retrieves all cards from the database.
     *
     * @return A list of all cards in the database.
     */
    @Override
    public List<Optional<Card>> findAll() {
        return null;
    }

    /**
     * Finds a card by its UUID in the database.
     *
     * @param uuid The UUID of the card to find.
     * @return An Optional containing the found card, or an empty Optional if not found.
     */
    @Override
    public Optional<Card> findById(UUID uuid) {
        String query = "SELECT uuid, name, damage FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        val result = database.executeSelectQuery(query, uuid);

        Optional<Card> card = Optional.empty();
        for (Row row : result.getRows()) {
            card = Optional.of(buildCardFromRow(row));
        }
        return card;
    }

    /**
     * Creates a new card in the database.
     *
     * @param card The card to be created.
     * @return The created card with its UUID set.
     */
    @Override
    public Card create(Card card) {
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, name, damage, package_uuid, user_uuid)" +
                " VALUES (?, ?, ?, ?, ?)";
        val database = Database.getInstance();
        database.executeInsertQuery(query,
                card.getUuid(), card.getName(), card.getDamage(), card.getPackageUuid(), card.getUserUuid());
        return card;
    }

    public List<Optional<Card>> findByOwner(User user) {
        String query = "SELECT uuid, name, damage, package_uuid, user_uuid FROM " + SCHEMA + TABLE +
                " WHERE user_uuid = ?";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query, user.getUuid());

        List<Optional<Card>> cards = new ArrayList<>();
        for (Row row : result.getRows()) {
            cards.add(Optional.of(buildCardFromRow(row)));
        }
        return cards;
    }

    public List<Optional<Card>> findAvailablePackage() {
        String packageQuery = "WITH random_package AS (SELECT DISTINCT package_uuid FROM " + SCHEMA + TABLE +
                " WHERE user_uuid IS NULL LIMIT 1)";
        String query = packageQuery + " " + "SELECT uuid, name, damage, package_uuid, user_uuid FROM " + SCHEMA + TABLE +
                " WHERE package_uuid = (SELECT package_uuid FROM random_package) LIMIT 5";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);

        List<Optional<Card>> cards = new ArrayList<>();
        for (Row row : result.getRows()) {
            cards.add(Optional.of(buildCardFromRow(row)));
        }
        return cards;
    }

    public void updateOwner(Card card, User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET user_uuid = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getUuid(), card.getUuid());
    }

    /**
     * Builds a Card entity from a database row.
     *
     * @param row The database row containing card data.
     * @return A Card entity built from the database row.
     */
    private Card buildCardFromRow(Row row) {
        return Card.builder()
                .uuid(row.getUuid("uuid"))
                .name(row.getString("name"))
                .damage(row.getInt("damage"))
                .build();
    }

    /**
     * Gets a singleton instance of the CardRepository.
     *
     * @return The singleton instance of the CardRepository.
     */
    public static synchronized CardRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardRepository();
        }
        return INSTANCE;
    }
}
