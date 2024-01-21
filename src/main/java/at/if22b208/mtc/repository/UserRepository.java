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

@Slf4j
public class UserRepository implements Repository<User, UUID> {
    private static UserRepository INSTANCE;
    private static final String TABLE = "user";

    private UserRepository() {
        // hide constructor
    }

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

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.empty();
    }

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

    @Override
    public User create(User user) {
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, username, password, balance) VALUES " +
                "((" + GENERATE_UUID_SEQUENCE_STRING + "), ?, ?, ?)";
        val database = Database.getInstance();
        UUID uuid = database.executeInsertQuery(query, user.getUsername(), user.getPassword(), user.getBalance());
        return user.withUuid(uuid);
    }

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

    public void updateDeck(User user) {
        String query = "UPDATE " + SCHEMA + TABLE + " SET deck = ? WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query,
                JsonUtils.getJsonStringFromArray(user.getDeck().stream().map(Card::getUuid).toArray()), user.getUuid());
    }

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
