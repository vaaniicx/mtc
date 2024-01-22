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
        String query = "SELECT uuid, name, damage, user_uuid, package_id FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
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
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, name, damage, package_id, user_uuid)" +
                " VALUES (?, ?, ?, ?, ?)";
        val database = Database.getInstance();
        database.executeInsertQuery(query,
                card.getUuid(), card.getName(), card.getDamage(), card.getPackageId(), card.getUserUuid());
        return card;
    }

    /**
     * Finds all cards owned by a specific user.
     *
     * @param user The user for whom to retrieve owned cards.
     * @return A list of Optional cards owned by the user.
     */
    public List<Optional<Card>> findByOwner(User user) {
        String query = "SELECT uuid, name, damage, package_id, user_uuid FROM " + SCHEMA + TABLE +
                " WHERE user_uuid = ?";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query, user.getUuid());

        List<Optional<Card>> cards = new ArrayList<>();
        for (Row row : result.getRows()) {
            cards.add(Optional.of(buildCardFromRow(row)));
        }
        return cards;
    }

    /**
     * Finds available cards in a package.
     *
     * @return A list of Optional cards available in a package.
     */
    public List<Optional<Card>> findAvailablePackage() {
        String packageQuery = "WITH random_package AS (SELECT DISTINCT package_id FROM " + SCHEMA + TABLE +
                " WHERE user_uuid IS NULL)";
        String query = packageQuery + " " + "SELECT uuid, name, damage, package_id, user_uuid FROM " + SCHEMA + TABLE +
                " WHERE package_id = (SELECT package_id FROM random_package ORDER BY package_id ASC LIMIT 1)";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);

        List<Optional<Card>> cards = new ArrayList<>();
        for (Row row : result.getRows()) {
            cards.add(Optional.of(buildCardFromRow(row)));
        }
        return cards;
    }

    /**
     * Updates the owner of a specific card.
     *
     * @param card The card to be updated.
     * @param user The new owner of the card.
     */
    public void updateOwner(Card card, User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET user_uuid = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getUuid(), card.getUuid());
    }

    /**
     * Finds the maximum package ID currently in use.
     *
     * @return The maximum package ID or null if no packages exist.
     */
    public Integer findNextPackageId() {
        String query = "SELECT MAX(package_id) as max FROM " + SCHEMA + TABLE;
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);
        for (Row row : result.getRows()) {
            return row.getInt("max");
        }
        return null;
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
                .damage(row.getDouble("damage"))
                .packageId(row.getInt("package_id"))
                .userUuid(row.getUuid("user_uuid"))
                .build();
    }

    /**
     * Gets a singleton instance of the {@code CardRepository}.
     *
     * @return The singleton instance of the {@code CardRepository}.
     */
    public static synchronized CardRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardRepository();
        }
        return INSTANCE;
    }
}
