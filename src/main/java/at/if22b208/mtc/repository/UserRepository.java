package at.if22b208.mtc.repository;

import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.database.Row;
import at.if22b208.mtc.entity.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserRepository implements Repository<User, UUID> {
    private static UserRepository INSTANCE;
    private static final String TABLE = "user";

    private UserRepository() {
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        String query = "SELECT uuid, username, password, balance FROM " + SCHEMA + TABLE + " WHERE username = ?";
        val database = Database.getINSTANCE();
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
        val database = Database.getINSTANCE();
        UUID uuid = database.executeInsertQuery(query, user.getUsername(), user.getPassword(), user.getBalance());
        return user.withUuid(uuid);
    }

    private User buildUserFromRow(Row row) {
        return User.builder()
                .uuid(row.getUuid("uuid"))
                .username(row.getString("username"))
                .password(row.getString("password"))
                .balance(BigInteger.valueOf(row.getLong("balance")))
                .build();
    }

    public static synchronized UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }
}
