package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.dto.user.UserCredentialsDto;
import at.if22b208.mtc.dto.user.UserDataDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.HashingException;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController implements Controller {
    private static UserController INSTANCE;

    private UserController() {
        // hide constructor
    }

    private Response createUserWithCredentials(UserCredentialsDto dto) throws HashingException {
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

    private Response updateUserData(String username, UserDataDto dto) {
        User user = UserService.getInstance().getByUsername(username);

        if (user != null) {
            // TODO: Update user
        }
        return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
    }

    private Response getUserByUsername(String username) throws JsonProcessingException {
        User user = UserService.getInstance().getByUsername(username);

        if (user != null) {
            UserDataDto dto = UserDataDto.builder() // TODO: set values
                    .name("")
                    .biography("")
                    .image("")
                    .build();

            return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromObject(dto));
        }
        return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
    }


    @Override
    public Response handleRequest(Request request) throws JsonProcessingException, HashingException {
        String root = request.getRoot();

        if (root.equalsIgnoreCase("users")) {
            String body = request.getBody();
            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    UserCredentialsDto dto = JsonUtils.getObjectFromJsonString(body, UserCredentialsDto.class);
                    return this.createUserWithCredentials(dto);
                }
            }

            if (!SessionUtils.isAuthorized(request.getHeader())) {
                return ResponseUtils.unauthorized();
            }

            if (request.getPathParts().size() == 2) {
                if (request.getMethod() == Method.GET) {
                    return this.getUserByUsername(request.getPathParts().get(1));
                }

                if (request.getMethod() == Method.PUT) {
                    UserDataDto dto = JsonUtils.getObjectFromJsonString(body, UserDataDto.class);
                    return this.updateUserData(request.getPathParts().get(1), dto);
                }
            }
        }
        return null;
    }

    public static synchronized UserController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserController();
        }
        return INSTANCE;
    }
}
