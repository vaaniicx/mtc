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

/**
 * Controller class for handling scoreboard-related operations.
 */
public class ScoreboardController implements Controller {
    private static ScoreboardController INSTANCE;

    private ScoreboardController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves the scoreboard containing user statistics sorted by Elo ranking.
     *
     * @return Response containing the scoreboard in JSON format.
     */
    private Response getScoreboard() {
        List<User> users = UserService.getInstance().getAll();
        List<UserStatsDto> dtos = users.stream()
                .map(UserMapper.INSTANCE::mapToUserStatsDto)
                .sorted(Comparator.comparing(UserStatsDto::elo).reversed())
                .toList();
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(dtos.toArray()));
    }

    /**
     * Handles incoming HTTP requests related to the scoreboard.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     * @throws JsonProcessingException if there is an issue processing JSON.
     */
    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        // Check if the request is authorized
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("scoreboard")) {
            if (request.getMethod() == Method.GET) {
                // Handle GET requests for the scoreboard
                return this.getScoreboard();
            }
        }
        return ResponseUtils.notImplemented();
    }

    /**
     * Gets the singleton instance of the {@code ScoreboardController}.
     *
     * @return The singleton instance of the {@code ScoreboardController}.
     */
    public static synchronized ScoreboardController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScoreboardController();
        }
        return INSTANCE;
    }
}
