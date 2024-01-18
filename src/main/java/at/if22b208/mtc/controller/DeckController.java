package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeckController implements Controller {
    private static DeckController INSTANCE;

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
            StringBuilder b = new StringBuilder();
            deck.forEach(b::append);
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, b.toString());
        }
        return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(deck.toArray()));
    }

    private Response configureDeck(String username, List<UUID> uuids) {
        User user = UserService.getInstance().getByUsername(username);
        if (user == null) {
            return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
        }

        // TODO: remove null check, make it nullsafe
        if (uuids == null || new HashSet<>(uuids).size() != 4) {
            return ResponseUtils.badRequest(MessageConstants.CONFIGURE_DECK_FAILURE);
        }

        Set<UUID> ownedCards = CardService.getInstance().getAllByOwner(user)
                .stream()
                .map(Card::getUuid)
                .collect(Collectors.toUnmodifiableSet());

        if (ownedCards.containsAll(uuids)) {
            user.setDeck(uuids.stream().map(CardService.getInstance()::getById).toList());
            UserService.getInstance().updateDeck(user);
            return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.CONFIGURE_DECK);
        }
        return ResponseUtils.conflict(MessageConstants.CONFIGURE_DECK_CARD_UNAVAILABLE);
    }

    @Override
    public Response handleRequest(Request request) {
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

    public static synchronized DeckController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeckController();
        }
        return INSTANCE;
    }
}
