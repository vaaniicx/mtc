package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.repository.UserRepository;
import at.if22b208.mtc.server.Service;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService implements Service {

    private static UserService INSTANCE;

    private UserService() {
    }

    public List<User> getAll() {
        return UserRepository.getInstance().findAll();
    }

    public Optional<User> getById(UUID uuid) {
        return UserRepository.getInstance().findById(uuid);
    }

    public static synchronized UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return INSTANCE;
    }

    @Override
    public Response handleRequest(Request request) {
        return null;
    }
}
