package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.repository.UserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private static UserService INSTANCE;

    private UserService() {
    }

    public List<User> getAll() {
        return UserRepository.getInstance().findAll();
    }

    public Optional<User> getById(UUID uuid) {
        return UserRepository.getInstance().findById(uuid);
    }

    public Optional<User> getByUsername(String username) {
        return UserRepository.getInstance().findByUsername(username);
    }

    public User createUser(User user) {
        Optional<User> optional = getByUsername(user.getUsername());
        if (optional.isPresent()) {
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
