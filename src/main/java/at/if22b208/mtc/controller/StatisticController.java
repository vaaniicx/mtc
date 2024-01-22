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

/**
 * Controller class for handling user statistics.
 */
public class StatisticController implements Controller {
    private static StatisticController INSTANCE;

    private StatisticController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves user statistics for the specified username.
     *
     * @param username The username for which to retrieve statistics.
     * @return Response containing user statistics in JSON format.
     */
    private Response getStats(String username) {
        User user = UserService.getInstance().getByUsername(username);
        UserStatsDto dto = UserMapper.INSTANCE.mapToUserStatsDto(user);
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromObject(dto));
    }

    /**
     * Handles incoming HTTP requests related to user statistics.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     * @throws JsonProcessingException if there is an issue processing JSON.
     */
    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        String root = request.getRoot();

        if (root.equalsIgnoreCase("stats")) {
            // Check if the request is authorized
            if (!SessionUtils.isAuthorized(request.getHeader())) {
                return ResponseUtils.unauthorized();
            }

            if (request.getMethod() == Method.GET) {
                if (request.getPathParts().size() == 1) {
                    // Retrieve the username from the user session
                    String username = SessionUtils.getUsernameFromHeader(request.getHeader());
                    return getStats(username);
                }
            }
        }
        return ResponseUtils.notImplemented();
    }

    /**
     * Gets the singleton instance of the {@code StatisticController}.
     *
     * @return The singleton instance of the {@code StatisticController}.
     */
    public static synchronized StatisticController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticController();
        }
        return INSTANCE;
    }
}
