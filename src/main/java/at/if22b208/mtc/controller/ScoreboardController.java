package at.if22b208.mtc.controller;

import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.if22b208.mtc.database.Transaction;
import at.if22b208.mtc.dto.user.UserStatsDto;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.DatabaseTransactionException;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling scoreboard-related operations.
 */
@Slf4j
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
    private Response getScoreboard()
            throws DatabaseTransactionException {
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
    public Response handleRequest(Request request)
            throws JsonProcessingException {
        // Check if the request is authorized
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        Transaction transaction = new Transaction();
        try {
            if (root.equalsIgnoreCase("scoreboard")) {
                if (request.getMethod() == Method.GET) {
                    Response response = this.getScoreboard();
                    transaction.commit();

                    return response;
                }
            }
        } catch (DatabaseTransactionException e) {
            try {
                transaction.rollback();
            } catch (DatabaseTransactionException rollbackException) {
                log.error("Failed to rollback transaction: {}", rollbackException.getMessage());
            }
            return ResponseUtils.error("Error performing database transaction. See logs for further information.");
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
