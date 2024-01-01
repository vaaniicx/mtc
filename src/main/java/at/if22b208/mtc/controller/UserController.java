package at.if22b208.mtc.controller;

import at.if22b208.mtc.dto.UserDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.HashingException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.*;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.HashingUtils;
import at.if22b208.mtc.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController implements Controller {
    private static UserController INSTANCE;

    private UserController() {
    }

    private Response getUserByUsername(String username) throws JsonProcessingException {
        User user = UserService.getInstance().getByUsername(username);
        if (user != null) {
            return new Response(HttpStatus.OK, ContentType.JSON, JsonUtils.getJsonStringFromObject(user));
        }
        return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "User not existing.");
    }

    private Response createUserWithCredentials(String username, String password) throws JsonProcessingException, HashingException {
        User user = User.builder()
                .username(username)
                .password(HashingUtils.hash(password, HashingUtils.generateSalt(username)))
                .build();
        user = UserService.getInstance().create(user);
        if (user != null) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, JsonUtils.getJsonStringFromObject(user));
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, "User already exists.");
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException, HashingException {
        String root = request.getPathParts().get(0);

        if (root.equalsIgnoreCase("users")) {
            if (request.getMethod() == Method.GET) {
                if (request.getPathParts().size() == 2) {
                    return this.getUserByUsername(request.getPathParts().get(1));
                }
            }

            if (request.getMethod() == Method.PUT) {
                if (request.getPathParts().size() == 2) {
                    // update user data
                }
            }

            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    String body = request.getBody().toLowerCase();
                    UserDto dto = JsonUtils.getObjectFromJsonString(body, UserDto.class);
                    return this.createUserWithCredentials(dto.getUsername(), dto.getPassword());
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
