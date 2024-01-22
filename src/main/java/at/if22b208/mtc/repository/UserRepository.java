package at.if22b208.mtc.repository;

import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.database.Result;
import at.if22b208.mtc.database.Row;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code UserRepository} class is responsible for handling database operations related to users.
 *
 * <p>This repository provides methods for finding, creating, and updating user data in the database.</p>
 *
 * @see Repository
 * @see User
 */
@Slf4j
public class UserRepository implements Repository<User, UUID> {
    private static UserRepository INSTANCE;
    private static final String TABLE = "user";

    private UserRepository() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users in the database.
     */
    public List<Optional<User>> findAll() {
        String query = "SELECT uuid, username, password, balance, deck, name, biography, image, elo, wins, losses FROM " + SCHEMA + TABLE;
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);

        List<Optional<User>> users = new ArrayList<>();
        for (Row row : result.getRows()) {
            users.add(Optional.of(buildUserFromRow(row)));
        }
        return users;
    }

    /**
     * Finds a user by their UUID in the database.
     *
     * @param uuid The UUID of the user to find.
     * @return An Optional containing the found user, or an empty Optional if not found.
     */
    @Override
    public Optional<User> findById(UUID uuid) {
        String query = "SELECT uuid, username, password, balance, deck, name, biography, image, elo, wins, losses " +
                "FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query, uuid);

        for (Row row : result.getRows()) {
            return Optional.of(buildUserFromRow(row));
        }
        return Optional.empty();
    }

    /**
     * Finds a user by their username in the database.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the found user, or an empty Optional if not found.
     */
    public Optional<User> findByUsername(String username) {
        String query = "SELECT uuid, username, password, balance, deck, name, biography, image, elo, wins, losses FROM "
                + SCHEMA + TABLE + " WHERE username = ?";
        val database = Database.getInstance();
        val result = database.executeSelectQuery(query, username);

        Optional<User> user = Optional.empty();
        for (Row row : result.getRows()) {
            user = Optional.of(buildUserFromRow(row));
        }
        return user;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user to be created.
     * @return The created user with its UUID set.
     */
    @Override
    public User create(User user) {
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, username, password, balance) VALUES " +
                "((" + GENERATE_UUID_SEQUENCE_STRING + "), ?, ?, ?)";
        val database = Database.getInstance();
        UUID uuid = database.executeInsertQuery(query, user.getUsername(), user.getPassword(), user.getBalance());
        return user.withUuid(uuid);
    }

    /**
     * Updates user data (name, biography, image) in the database.
     *
     * @param user The user whose data needs to be updated.
     */
    public void updateUserData(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET name = ?, biography = ?, image = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getName(), user.getBiography(), user.getImage(), user.getUuid());
    }

    /**
     * Updates the balance of the user in the database.
     *
     * @param user The user whose balance needs to be updated.
     */
    public void updateBalance(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET balance = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getBalance(), user.getUuid());
    }

    /**
     * Updates the ELO rating of the user in the database.
     *
     * @param user The user whose ELO rating needs to be updated.
     */
    public void updateElo(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET elo = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getElo(), user.getUuid());
    }

    /**
     * Updates the losses count of the user in the database.
     *
     * @param user The user whose losses count needs to be updated.
     */
    public void updateLoss(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET losses = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getLosses(), user.getUuid());
    }

    /**
     * Updates the wins count of the user in the database.
     *
     * @param user The user whose wins count needs to be updated.
     */
    public void updateWin(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET wins = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, user.getWins(), user.getUuid());
    }

    /**
     * Updates the deck of the user in the database.
     *
     * @param user The user whose deck needs to be updated.
     */
    public void updateDeck(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET deck = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query,
                JsonUtils.getJsonStringFromArray(user.getDeck().stream().map(Card::getUuid).toArray()), user.getUuid());
    }

    /**
     * Builds a User entity from a database row.
     *
     * @param row The database row containing user data.
     * @return A User entity built from the database row.
     */
    private User buildUserFromRow(Row row) {
        return User.builder()
                .uuid(row.getUuid("uuid"))
                .username(row.getString("username"))
                .password(row.getString("password"))
                .balance(BigInteger.valueOf(row.getLong("balance")))
                .deck(row.getString("deck") == null ?
                        new ArrayList<>() :
                        JsonUtils.getListFromJsonString(row.getString("deck"), UUID.class)
                                .stream()
                                .map(CardService.getInstance()::getById)
                                .toList()
                )
                .name(row.getString("name"))
                .biography(row.getString("biography"))
                .image(row.getString("image"))
                .elo(row.getInt("elo"))
                .wins(row.getInt("wins"))
                .losses(row.getInt("losses"))
                .build();
    }

    /**
     * Gets the singleton instance of the {@code UserRepository}.
     *
     * @return The singleton instance of the {@code UserRepository}.
     */
    public static synchronized UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }
}
