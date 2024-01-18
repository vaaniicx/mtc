package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
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

import java.util.List;

/**
 * Controller class for handling card-related operations.
 */
public class CardController implements Controller {
    private static CardController INSTANCE;

    private CardController() {
        // hide constructor
    }

    /**
     * Retrieves a list of cards associated with the specified username.
     *
     * @param username The username of the user for whom to retrieve cards.
     * @return Response containing the list of cards in JSON format.
     */
    private Response getCards(String username) {
        User user = UserService.getInstance().getByUsername(username);

        if (user != null) {
            List<Card> cards = CardService.getInstance().getAllByOwner(user);

            if (cards.isEmpty()) {
                return ResponseUtils.noContent(MessageConstants.USER_NO_CARDS);
            }
            return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(cards.toArray()));
        } else {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }
    }

    @Override
    public Response handleRequest(Request request) {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        // Retrieve the username from the user session
        String username = SessionUtils.getUsernameFromHeader(request.getHeader());

        String root = request.getRoot();
        if ("cards".equals(root) && request.getMethod() == Method.GET) {
            return this.getCards(username);
        }
        return ResponseUtils.notImplemented();
    }

    /**
     * Gets the singleton instance of the {@code CardController}.
     *
     * @return The singleton instance of the {@code CardController}.
     */
    public static synchronized CardController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardController();
        }
        return INSTANCE;
    }
}
