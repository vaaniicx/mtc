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
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves a list of cards associated with the specified username.
     *
     * @param user The user for whom to retrieve cards.
     * @return Response containing the list of cards in JSON format.
     */
    private Response getCards(User user) {
        List<Card> cards = CardService.getInstance().getAllByOwner(user);

        if (cards.isEmpty()) {
            // Return a response with no content and a message indicating no cards found.
            return ResponseUtils.noContent(MessageConstants.USER_NO_CARDS);
        }
        // Return a response with JSON content containing the list of cards.
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(cards.toArray()));
    }

    /**
     * Handles the incoming HTTP request for card-related operations.
     *
     * @param request The incoming HTTP request.
     * @return Response indicating the outcome of the request.
     */
    @Override
    public Response handleRequest(Request request) {
        // Check if the request is authorized
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        // Retrieve the username from the user session
        String username = SessionUtils.getUsernameFromHeader(request.getHeader());
        User user = UserService.getInstance().getByUsername(username);
        // Check if the user is not found
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        String root = request.getRoot();
        if ("cards".equals(root) && request.getMethod() == Method.GET) {
            // Handle GET request for retrieving cards
            return this.getCards(user);
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
