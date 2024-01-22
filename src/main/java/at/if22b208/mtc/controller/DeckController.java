package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.service.TradingDealService;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller class for handling deck-related operations.
 */
public class DeckController implements Controller {
    private static DeckController INSTANCE;

    private DeckController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves the user's deck based on the specified username and type.
     *
     * @param username The username of the user for whom to retrieve the deck.
     * @param type     The type of deck format (e.g., "format=plain").
     * @return Response containing the deck information in the specified format.
     */
    private Response getDeck(String username, String type) {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        List<Card> deck = UserService.getInstance().getDeckByOwner(user);
        if (deck.isEmpty()) {
            return ResponseUtils.noContent(MessageConstants.DECK_NO_CARDS);
        }

        if ("format=plain".equalsIgnoreCase(type)) {
            // Return the deck in plain text format
            StringBuilder b = new StringBuilder();
            deck.forEach(b::append);
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, b.toString());
        }
        // Return the deck in JSON format
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(deck.toArray()));
    }

    /**
     * Configures the user's deck based on the specified username and card UUIDs.
     *
     * @param username The username of the user for whom to configure the deck.
     * @param uuids    List of UUIDs representing the cards to include in the deck.
     * @return Response indicating the success or failure of the deck configuration.
     */
    private Response configureDeck(String username, List<UUID> uuids) {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        // Check for null or incorrect number of cards
        if (uuids == null || new HashSet<>(uuids).size() != 4) {
            return ResponseUtils.badRequest(MessageConstants.CONFIGURE_DECK_FAILURE);
        }

        Set<UUID> ownedCards = CardService.getInstance().getAllByOwner(user)
                .stream()
                .map(Card::getUuid)
                .collect(Collectors.toUnmodifiableSet());

        if (ownedCards.containsAll(uuids) && !hasCardLockedInTradingDeal(uuids)) {
            // Set the user's deck with the specified card UUIDs
            user.setDeck(uuids.stream().map(CardService.getInstance()::getById).toList());
            UserService.getInstance().updateDeck(user);
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.CONFIGURE_DECK);
        }
        // Return a conflict response if any of the specified cards are not owned by the user or locked in a trading deal
        return ResponseUtils.conflict(MessageConstants.CONFIGURE_DECK_CARD_UNAVAILABLE);
    }

    /**
     * Checks if any of the specified cards are locked in an active trading deal.
     *
     * @param uuids List of UUIDs representing the cards to check.
     * @return True if any of the cards are locked in a trading deal; otherwise, false.
     */
    private boolean hasCardLockedInTradingDeal(List<UUID> uuids) {
        List<TradingDeal> deals = TradingDealService.getInstance().getAll();
        return deals.stream()
                .map(TradingDeal::getCardUuid)
                .toList().stream()
                .anyMatch(uuids::contains);
    }

    @Override
    public Response handleRequest(Request request) {
        // Check if the request is authorized
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        // Retrieve the username from the user session
        String username = SessionUtils.getUsernameFromHeader(request.getHeader());

        String root = request.getRoot();
        Response response = ResponseUtils.notImplemented();
        if ("deck".equals(root)) {
            switch (request.getMethod()) {
                case GET -> response = getDeck(username, request.getParams());
                case PUT ->
                        response = configureDeck(username, JsonUtils.getListFromJsonString(request.getBody(), UUID.class));
            }
        }
        return response;
    }

    /**
     * Gets the singleton instance of {@code DeckController}.
     *
     * @return The singleton instance of the {@code DeckController}.
     */
    public static synchronized DeckController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeckController();
        }
        return INSTANCE;
    }
}
