package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.config.MtcConstants;
import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.BalanceTransactionException;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TransactionController implements Controller {
    private static TransactionController INSTANCE;

    private TransactionController() {
        // hide constructor
    }

    /**
     * <p>Acquires a package of cards for the user with the provided username.</p>
     *
     * <p>This method retrieves the user associated with the given username, updates their balance
     * by subtracting the cost of the package, and assigns ownership of the cards in the acquired
     * package to the user. The package is expected to contain exactly 5 cards.</p>
     *
     * @param username The username of the user acquiring the package.
     * @return A {@link Response} indicating the result of the package acquisition.
     */
    private Response acquirePackage(String username) throws JsonProcessingException {
        try {
            User user = UserService.getInstance().getByUsername(username); // Get the user associated with the username

            if (user != null) { // Check if the user exists
                // Update the user's balance by subtracting the package cost
                UserService.getInstance()
                        .updateBalance(user, MtcConstants.PACKAGE_COST, new SubtractOperation());

                CardService cardService = CardService.getInstance();
                List<Card> pack = cardService.getPackage();

                // Iterate over cards in the acquired package and update their owner to the current user
                pack.forEach(card -> cardService.updateOwner(card, user));

                List<CardDto> dtoList = pack.stream()
                        .map(CardMapper.INSTANCE::mapCardToCardDto)
                        .toList();

                return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(dtoList.toArray()));
            } else {
                return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
            }
        } catch (BalanceTransactionException e) {
            return ResponseUtils.forbidden(MessageConstants.INSUFFICIENT_FUNDS);
        } catch (InvalidPackageException e) {
            return ResponseUtils.notFound(MessageConstants.NO_PACKAGE_AVAILABLE);
        }
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("transactions")) {
            if (request.getSecondPathPart().equals("packages") && request.getMethod() == Method.POST) {
                // Retrieve the username from the user session
                String token = SessionUtils.getBearerToken(request.getHeader());
                String username = SessionUtils.getUsernameFromUserSession(token);

                return acquirePackage(username);
            }
        }
        return null;
    }

    public static synchronized TransactionController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransactionController();
        }
        return INSTANCE;
    }
}
