package at.if22b208.mtc.controller;

import at.if22b208.mtc.dto.user.UserStatsDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import at.if22b208.mtc.util.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Comparator;
import java.util.List;

public class ScoreboardController implements Controller {
    private static ScoreboardController INSTANCE;

    private ScoreboardController() {
        // hide constructor
    }

    private Response getScoreboard() {
        List<User> users = UserService.getInstance().getAll();
        List<UserStatsDto> dtos = users.stream()
                .map(UserMapper.INSTANCE::mapToUserStatsDto)
                .sorted(Comparator.comparing(UserStatsDto::elo))
                .toList();
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(dtos.toArray()));
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("scoreboard")) {
            if (request.getMethod() == Method.GET) {
                return this.getScoreboard();
            }
        }
        return ResponseUtils.notImplemented();
    }

    public static synchronized ScoreboardController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScoreboardController();
        }
        return INSTANCE;
    }
}
