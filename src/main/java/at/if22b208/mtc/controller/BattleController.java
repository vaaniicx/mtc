package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.entity.Battle;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.BalanceTransactionException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.BattleService;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import at.if22b208.mtc.util.balance.AddOperation;
import at.if22b208.mtc.util.balance.SubtractOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

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

            if (battle.getWinner().getUuid() == battle.getPlayerA().getUuid()) {
                updateUserStatisticData(battle.getPlayerA(), battle.getPlayerB());
            } else {
                updateUserStatisticData(battle.getPlayerB(), battle.getPlayerA());
            }

            int roundsPlayed = battle.getRounds().size();
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, "Rounds played: " + roundsPlayed + ", Winner is: " + battle.getWinner().getUsername());
        } catch (InterruptedException e) {
            return ResponseUtils.error(":D"); // Handle InterruptedException with an error response.
        } catch (BalanceTransactionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the user statistics after a battle.
     *
     * @param winner The user who won the battle.
     * @param loser  The user who lost the battle.
     * @throws BalanceTransactionException If a balance transaction error occurs.
     */
    private static void updateUserStatisticData(User winner, User loser) throws BalanceTransactionException {
        // Update winner data
        UserService.getInstance().updateElo(winner, BigInteger.valueOf(3), new AddOperation());
        UserService.getInstance().updateWin(winner, BigInteger.valueOf(1), new AddOperation());

        // Update loser data
        UserService.getInstance().updateElo(loser, BigInteger.valueOf(5), new SubtractOperation());
        UserService.getInstance().updateLoss(loser, BigInteger.valueOf(1), new AddOperation());
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
