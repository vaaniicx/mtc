package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.entity.Battle;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.BattleService;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.EloSystem;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller responsible for handling battle-related requests.
 */
@Slf4j
public class BattleController implements Controller {
    private static BattleController INSTANCE;

    private BattleController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Handles the request to wait for a battle to be ready.
     *
     * @param user The user waiting for the battle.
     * @return Response indicating the outcome of the battle.
     */
    private Response waitForBattleToBeReady(User user) {
        try {
            Battle battle = BattleService.getInstance().enterBattleQueue(user);

            if (battle.isDraw()) {
                return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.BATTLE_DRAW);
            }

            User playerA = battle.getPlayerA();
            User playerB = battle.getPlayerB();

            // Calculate new ELO ratings
            EloSystem.updateRatings(playerA, playerB, isPlayerAWon(battle));

            // Persist new ELO ratings
            updateUserStatisticData(playerA);
            updateUserStatisticData(playerB);

            int roundsPlayed = battle.getRounds().size();
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, "Rounds played: " + roundsPlayed + ", Winner is: " + battle.getWinner().getUsername());
        } catch (InterruptedException e) {
            return ResponseUtils.error(":D"); // Handle InterruptedException with an error response.
        }
    }

    private static boolean isPlayerAWon(Battle battle) {
        return battle.getWinner().getUuid() == battle.getPlayerA().getUuid();
    }

    private static void updateUserStatisticData(User user) {
        UserService.getInstance().updateElo(user);
    }

    /**
     * Handles the incoming HTTP request for battles.
     *
     * @param request The incoming HTTP request.
     * @return Response indicating the outcome of the request.
     * @throws JsonProcessingException If JSON processing error occurs.
     */
    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("battles")) {
            if (request.getMethod() == Method.POST) {
                // Retrieve the username from the user session
                String username = SessionUtils.getUsernameFromHeader(request.getHeader());
                User user = UserService.getInstance().getByUsername(username);
                return waitForBattleToBeReady(user);
            }
        }
        return ResponseUtils.notImplemented();
    }

    /**
     * Gets the singleton instance of the BattleController.
     *
     * @return The instance of the BattleController.
     */
    public static synchronized BattleController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleController();
        }
        return INSTANCE;
    }
}
