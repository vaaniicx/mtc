package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.dto.user.UserCredentialsDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.HashingException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.server.session.SessionManager;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.HashingUtils;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Controller class for handling user sessions and login functionality.
 */
@Slf4j
public class SessionController implements Controller {
    private static SessionController INSTANCE;

    private SessionController() {
        // hide constructor
    }

    /**
     * Attempts to log in a user with the provided username and password.
     *
     * @param dto The {@link UserCredentialsDto} containing username and password.
     * @return A {@link Response} object representing the result of the login attempt.
     */
    private Response login(UserCredentialsDto dto) {
        try {
            // Hash the provided password
            String hash = HashingUtils.hash(dto.getPassword(), HashingUtils.generateSalt(dto.getUsername()));
            // Retrieve the user by username
            User user = UserService.getInstance().getByUsername(dto.getUsername());
            if (user != null) {
                // Check if the user exists and if the provided username and password match
                if (Objects.equals(user.getUsername(), dto.getUsername()) && Objects.equals(user.getPassword(), hash)) {
                    // Successful login
                    String token = SessionManager.createSession(user.getUsername());
                    return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromObject(token));
                }
            }
        } catch (HashingException e) {
            log.warn("Error during login for username '{}': {}", dto.getUsername(), e.getMessage());
        }
        // Unsuccessful login
        // Not exposing information about already existing accounts or whether username or password is not matching
        return ResponseUtils.unauthorized(MessageConstants.USER_LOGIN_INVALID_CREDENTIALS);
    }

    /**
     * Handles incoming HTTP requests related to user sessions and login.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     */
    @Override
    public Response handleRequest(Request request) {
        String root = request.getRoot();

        if (root.equalsIgnoreCase("sessions")) {
            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    String body = request.getBody().toLowerCase();
                    UserCredentialsDto dto = JsonUtils.getObjectFromJsonString(body, UserCredentialsDto.class);
                    if (dto == null) {
                        return ResponseUtils.notImplemented();
                    }
                    return login(dto);
                }
            }
        }
        return ResponseUtils.notImplemented();
    }

    /**
     * Gets the singleton instance of the {@code SessionController}.
     *
     * @return The singleton instance of the {@code SessionController}.
     */
    public static synchronized SessionController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionController();
        }
        return INSTANCE;
    }
}
