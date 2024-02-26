package at.if22b208.mtc.controller;

import java.util.List;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.config.MtcConstants;
import at.if22b208.mtc.database.Transaction;
import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.BalanceTransactionException;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import at.if22b208.mtc.util.balance.SubtractOperation;
import at.if22b208.mtc.util.mapper.CardMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling card acquisition transactions.
 */
@Slf4j
public class TransactionController implements Controller {
    private static TransactionController INSTANCE;

    private TransactionController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Acquires a package of cards for the user with the provided username.
     *
     * @param username The username of the user acquiring the package.
     * @return A {@link Response} indicating the result of the package acquisition.
     */
    private Response acquirePackage(String username)
            throws DatabaseTransactionException {
        try {
            User user = UserService.getInstance().getByUsername(username); // Get the user associated with the username
            if (user == null) {
                return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
            }

            // Update the user's balance by subtracting the package cost
            UserService.getInstance()
                    .updateBalance(user, MtcConstants.PACKAGE_COST, new SubtractOperation());

            CardService cardService = CardService.getInstance();

            List<Card> pack = cardService.getPackage();
            // Iterate over cards in the acquired package and update their owner to the current user
            for (Card card : pack) {
                cardService.updateOwner(card, user);
            }

            List<CardDto> dtoList = pack.stream()
                    .map(CardMapper.INSTANCE::map)
                    .toList();
            return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(dtoList.toArray()));
        } catch (BalanceTransactionException e) {
            return ResponseUtils.forbidden(MessageConstants.INSUFFICIENT_FUNDS);
        } catch (InvalidPackageException e) {
            return ResponseUtils.notFound(MessageConstants.NO_PACKAGE_AVAILABLE);
        }
    }

    /**
     * Handles incoming HTTP requests related to card acquisition transactions.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     */
    @Override
    public Response handleRequest(Request request) {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        Transaction transaction = new Transaction();
        try {
            if (root.equalsIgnoreCase("transactions")) {
                if (request.getSecondPathPart().equals("packages") && request.getMethod() == Method.POST) {
                    // Retrieve the username from the user session
                    String username = SessionUtils.getUsernameFromHeader(request.getHeader());

                    Response response = acquirePackage(username);
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
     * Gets the singleton instance of the {@code TransactionController}.
     *
     * @return The singleton instance of the {@code TransactionController}.
     */
    public static synchronized TransactionController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransactionController();
        }
        return INSTANCE;
    }
}
