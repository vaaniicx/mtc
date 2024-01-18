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
import at.if22b208.mtc.util.mapper.UserMapper;
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

    private Response updateUserData(String username, UserDataDto dto) throws JsonProcessingException {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        user.setName(dto.getName());
        user.setBiography(dto.getBiography());
        user.setImage(dto.getImage());
        UserService.getInstance().updateUserData(user);
        return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.USER_UPDATED);
    }

    private Response getUserByUsername(String username) throws JsonProcessingException {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        UserDataDto dto = UserMapper.INSTANCE.map(user);
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromObject(dto));
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        String root = request.getRoot();

        if (root.equalsIgnoreCase("users")) {
            String body = request.getBody();
            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    UserCredentialsDto dto = JsonUtils.getObjectFromJsonString(body, UserCredentialsDto.class);
                    try {
                        return this.createUserWithCredentials(dto);
                    } catch (HashingException e) {
                        // TODO: Clean up
                        log.warn(e.getMessage());
                    }
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
