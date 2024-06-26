package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.database.Transaction;
import at.if22b208.mtc.dto.user.UserCredentialsDto;
import at.if22b208.mtc.dto.user.UserDataDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.HashingException;
import at.if22b208.mtc.exception.NameNotValidException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.HashingUtils;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import at.if22b208.mtc.util.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling user-related operations.
 */
@Slf4j
public class UserController implements Controller {
    private static UserController INSTANCE;

    private UserController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Creates a new user with the provided credentials.
     *
     * @param dto The user credentials DTO containing username and password.
     * @return Response indicating the success or failure of the user creation.
     * @throws HashingException If an error occurs during password hashing.
     */
    private Response createUserWithCredentials(UserCredentialsDto dto)
            throws HashingException, DatabaseTransactionException {
        User user = User.builder()
                .username(dto.getUsername())
                .password(HashingUtils.hash(dto.getPassword(), HashingUtils.generateSalt(dto.getUsername())))
                .build();
        user = UserService.getInstance().create(user);

        if (user != null) {
            return ResponseUtils.created(MessageConstants.USER_CREATED);
        }
        return ResponseUtils.conflict(MessageConstants.USER_ALREADY_REGISTERED);
    }

    /**
     * Updates user data for the specified username.
     *
     * @param username The username of the user to update.
     * @param dto      The user data DTO containing updated information.
     * @return Response indicating the success or failure of the user data update.
     */
    private Response updateUserData(String username, UserDataDto dto)
            throws DatabaseTransactionException {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        user.setName(dto.getName());
        user.setBiography(dto.getBiography());
        user.setImage(dto.getImage());
        try {
            UserService.getInstance().updateUserData(user);
        } catch (NameNotValidException e) {
            return ResponseUtils.conflict(MessageConstants.USER_UPDATE_FAILURE);
        }
        return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.USER_UPDATED);
    }

    /**
     * Retrieves user information for the specified username.
     *
     * @param username The username of the user to retrieve.
     * @return Response containing user information in JSON format.
     */
    private Response getUserByUsername(String username)
            throws DatabaseTransactionException {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        UserDataDto dto = UserMapper.INSTANCE.mapToUserDataDto(user);
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromObject(dto));
    }

    /**
     * Handles incoming HTTP requests related to user operations.
     * Validates the user's authorization and processes requests for user creation, retrieval, and updates.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     */
    @Override
    public Response handleRequest(Request request) {
        String root = request.getRoot();

        if (!root.equalsIgnoreCase("users")) {
            return ResponseUtils.notImplemented();
        }

        Transaction transaction = new Transaction();
        try {
            String body = request.getBody();
            if (request.getMethod() == Method.POST && request.getPathParts().size() == 1) {
                UserCredentialsDto dto = JsonUtils.getObjectFromJsonString(body, UserCredentialsDto.class);
                try {
                    return performUserCreation(dto, transaction);
                } catch (HashingException e) {
                    log.warn(e.getMessage());
                }
            }

            if (!SessionUtils.isAuthorized(request.getHeader())) {
                return ResponseUtils.unauthorized();
            }

            if (request.getPathParts().size() == 2) {
                if (request.getMethod() == Method.GET) {
                    return performUserRetrieval(this.getUserByUsername(request.getPathParts().get(1)), transaction);
                }

                if (request.getMethod() == Method.PUT) {
                    return performUserUpdate(request, body, transaction);
                }
            }
        } catch (DatabaseTransactionException e) {
            try {
                transaction.rollback();
            } catch (DatabaseTransactionException rollbackException) {
                log.error("Failed to rollback transaction: {}", rollbackException.getMessage());
            }
            return ResponseUtils.error("Error performing database transaction.");
        }

        return ResponseUtils.notImplemented();
    }

    private Response performUserCreation(UserCredentialsDto dto, Transaction transaction)
            throws DatabaseTransactionException, HashingException {
        if (dto == null) {
            return ResponseUtils.notImplemented();
        }
        return performUserRetrieval(this.createUserWithCredentials(dto), transaction);
    }

    private Response performUserRetrieval(Response request, Transaction transaction)
            throws DatabaseTransactionException {
        Response response = request;
        transaction.commit();

        return response;
    }

    private Response performUserUpdate(Request request, String body, Transaction transaction)
            throws DatabaseTransactionException {
        UserDataDto dto = JsonUtils.getObjectFromJsonString(body, UserDataDto.class);

        return performUserRetrieval(this.updateUserData(request.getPathParts().get(1), dto), transaction);
    }

    /**
     * Gets the singleton instance of the {@code UserController}.
     *
     * @return The singleton instance of the {@code UserController}.
     */
    public static synchronized UserController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserController();
        }
        return INSTANCE;
    }
}
