package at.if22b208.mtc.controller;

import at.if22b208.mtc.dto.user.UserDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.HashingException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.*;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.HashingUtils;
import at.if22b208.mtc.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class SessionController implements Controller {
    private static SessionController INSTANCE;

    private SessionController() {
    }

    /**
     * Attempts to log in a user with the provided username and password.
     *
     * @param dto The {@link UserDto} containing username and password.
     * @return A {@link Response} object representing the result of the login attempt.
     */
    private Response login(UserDto dto) {
        try {
            // Hash the provided password
            String hash = HashingUtils.hash(dto.getPassword(), HashingUtils.generateSalt(dto.getUsername()));

            // Retrieve the user by username
            Optional<User> optional = UserService.getInstance().getByUsername(dto.getUsername());
            if (optional.isPresent()) {
                User user = optional.get();

                // Check if the user exists and if the provided username and password match
                if (Objects.equals(user.getUsername(), dto.getUsername()) && Objects.equals(user.getPassword(), hash)) {
                    // Successful login
                    return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, "Login successful.");
                }
            }
        } catch (HashingException e) {
            log.error("Error during login for username '{}': {}", dto.getUsername(), e.getMessage());
        }
        // Unsuccessful login
        // Not exposing information about already existing accounts or whether username or password is not matching
        return new Response(HttpStatus.FORBIDDEN, ContentType.PLAIN_TEXT, "Invalid username or password.");
    }

    public static synchronized SessionController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionController();
        }
        return INSTANCE;
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        String root = request.getPathParts().get(0);

        if (root.equalsIgnoreCase("sessions")) {
            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    String body = request.getBody().toLowerCase();
                    UserDto dto = JsonUtils.getObjectFromJsonString(body, UserDto.class);
                    return login(dto);
                }
            }
        }
        return null;
    }
}
