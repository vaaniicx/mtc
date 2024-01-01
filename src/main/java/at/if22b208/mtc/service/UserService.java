package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.repository.UserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class UserService implements Service<User, UUID> {
    private static UserService INSTANCE;

    private UserService() {
    }

    public List<User> getAll() {
        return UserRepository.getInstance().findAll();
    }

    @Override
    public User getById(UUID uuid) {
        return UserRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    public User getByUsername(String username) {
        return UserRepository.getInstance()
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public User create(User user) {
        if (getByUsername(user.getUsername()) == null) {
            return null;
        }

        user.setBalance(BigInteger.valueOf(20));
        return UserRepository.getInstance().create(user);
    }

    public static synchronized UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return INSTANCE;
    }
}
