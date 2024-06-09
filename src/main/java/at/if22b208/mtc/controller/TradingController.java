package at.if22b208.mtc.controller;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.database.Transaction;
import at.if22b208.mtc.dto.trading.TradingDealDto;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.InvalidTradingDealException;
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
import at.if22b208.mtc.util.mapper.TradingDealMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling trading deals.
 */
@Slf4j public class TradingController implements Controller {
    private static TradingController INSTANCE;

    /**
     * Retrieves all trading deals from the system.
     *
     * @return A response containing a list of trading deals in JSON format, or a no-content response if no deals are
     * available.
     */
    private Response getTradingDeals()
            throws DatabaseTransactionException {
        List<TradingDeal> deals = TradingDealService.getInstance().getAll();
        if (!deals.isEmpty()) {
            return ResponseUtils.ok(ContentType.JSON, JsonUtils.getJsonStringFromArray(deals.toArray()));
        }
        return ResponseUtils.noContent(MessageConstants.NO_TRADING_DEALS);
    }

    /**
     * Creates a new trading deal based on the provided data transfer object (DTO).
     * Validates the ownership and status of the trading card before creating the deal.
     *
     * @param user The user initiating the trading deal.
     * @param dto  The trading deal data transfer object.
     * @return A response indicating the success or failure of the trading deal creation.
     */
    private Response createTradingDeal(User user, TradingDealDto dto)
            throws DatabaseTransactionException {
        Card tradingCard = CardService.getInstance().getById(dto.getCardUuid());

        // Can only create a deal for a card that is owned by the requesting user or card is not in deck
        if (tradingCard == null || !hasCardOwned(user, tradingCard) || hasCardLocked(user, tradingCard)) {
            return ResponseUtils.forbidden(MessageConstants.TRADING_DEAL_CARD_LOCKED);
        }

        try {
            TradingDealService.getInstance().create(TradingDealMapper.INSTANCE.map(dto));
        } catch (InvalidTradingDealException e) {
            return ResponseUtils.conflict(MessageConstants.TRADING_DEAL_ALREADY_EXISTS);
        }
        return ResponseUtils.created(MessageConstants.TRADING_DEAL_CREATE);
    }

    /**
     * Deletes a trading deal initiated by the user.
     * Validates the ownership of the trading card associated with the deal.
     *
     * @param user     The user initiating the deletion.
     * @param dealUuid The UUID of the trading deal to be deleted.
     * @return A response indicating the success or failure of the trading deal deletion.
     */
    private Response deleteTradingDeal(User user, UUID dealUuid)
            throws DatabaseTransactionException {
        TradingDeal deal = TradingDealService.getInstance().getById(dealUuid);
        if (deal == null) {
            return ResponseUtils.notFound(MessageConstants.TRADING_DEAL_NOT_FOUND);
        }

        Card tradingCard = CardService.getInstance().getById(deal.getCardUuid());

        // Can only delete a deal for a card that is owned by the requesting user
        if (tradingCard == null || !hasCardOwned(user, tradingCard)) {
            return ResponseUtils.forbidden(MessageConstants.TRADING_DEAL_CARD_NOT_OWNED);
        }

        TradingDealService.getInstance().deleteById(dealUuid);
        return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.TRADING_DEAL_DELETE);
    }

    /**
     * Carries out a trading deal by transferring ownership of cards between users.
     * Validates the ownership and trading requirements before completing the deal.
     *
     * @param user     The user initiating the deal.
     * @param dealUuid The UUID of the trading deal to be carried out.
     * @param cardUuid The UUID of the card offered by the user to complete the deal.
     * @return A response indicating the success or failure of carrying out the trading deal.
     */
    private Response carryOutDeal(User user, UUID dealUuid, UUID cardUuid)
            throws DatabaseTransactionException {
        TradingDeal deal = TradingDealService.getInstance().getById(dealUuid);
        if (deal == null) {
            return ResponseUtils.notFound(MessageConstants.TRADING_DEAL_NOT_FOUND);
        }

        Card offeredCard = CardService.getInstance().getById(cardUuid);

        // Can only delete a deal for a card that is owned by the requesting user
        if (offeredCard == null || isUserFromDeal(user, deal) || !hasCardOwned(user, offeredCard) ||
                hasCardLocked(user, offeredCard) || !hasTradingRequirements(deal, offeredCard)) {
            return ResponseUtils.forbidden(MessageConstants.TRADING_DEAL_CARRY_OUT_FAILURE);
        }

        Card tradingCard = CardService.getInstance().getById(deal.getCardUuid());
        User tradingUser = UserService.getInstance().getById(tradingCard.getUserUuid());

        CardService.getInstance().updateOwner(offeredCard, tradingUser);
        CardService.getInstance().updateOwner(tradingCard, user);

        TradingDealService.getInstance().deleteById(dealUuid);
        return ResponseUtils.ok(ContentType.PLAIN_TEXT, MessageConstants.TRADING_DEAL_CREATE);
    }

    /**
     * Checks if the user owns the specified card.
     *
     * @param user The user to check ownership for.
     * @param card The card to check ownership of.
     * @return True if the user owns the card, false otherwise.
     */
    private boolean hasCardOwned(User user, Card card) {
        return card.getUserUuid().equals(user.getUuid());
    }

    /**
     * Checks if the user's deck contains the specified card.
     *
     * @param user The user to check the deck for.
     * @param card The card to check if it is in the deck.
     * @return True if the card is in the user's deck, false otherwise.
     */
    private boolean hasCardLocked(User user, Card card) {
        return user.getDeck().stream().anyMatch(c -> c.getUuid().equals(card.getUuid()));
    }

    /**
     * Checks if the card meets the trading requirements specified in the deal.
     *
     * @param deal        The trading deal containing the requirements.
     * @param offeredCard The card offered by the user.
     * @return True if the card meets the trading requirements, false otherwise.
     */
    private boolean hasTradingRequirements(TradingDeal deal, Card offeredCard) {
        return deal.getCardType().equals(offeredCard.getCardType()) &&
                deal.getMinimumDamage() <= offeredCard.getDamage();
    }

    /**
     * Checks if the specified user is the owner of the card associated with the trading deal.
     *
     * @param user The user to check against.
     * @param deal The trading deal containing the card UUID.
     * @return True if the user is the owner, false otherwise.
     */
    private boolean isUserFromDeal(User user, TradingDeal deal)
            throws DatabaseTransactionException {
        return user.getUuid().equals(CardService.getInstance().getById(deal.getCardUuid()).getUserUuid());
    }

    /**
     * Handles incoming HTTP requests related to trading deals.
     * Validates the user's authorization and processes requests for trading deals.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     * @throws JsonProcessingException If there is an error processing JSON data.
     */
    @Override public Response handleRequest(Request request)
            throws JsonProcessingException {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        Transaction transaction = new Transaction();
        try {
            // Retrieve the username from the user session
            String username = SessionUtils.getUsernameFromHeader(request.getHeader());
            User user = UserService.getInstance().getByUsername(username);
            if (user == null) {
                return ResponseUtils.notFound(MessageConstants.USER_NOT_FOUND);
            }

            String root = request.getRoot();
            if (!root.equalsIgnoreCase("tradings")) {
                return ResponseUtils.notImplemented();
            }

            if (request.getPathParts().size() == 1) {
                return performRetrieveOrCreateDeal(request, user, transaction);
            }

            if (request.getPathParts().size() == 2) {
                return performCarryOutOrDealDeletion(request, user);
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

    private Response performRetrieveOrCreateDeal(Request request, User user, Transaction transaction)
            throws DatabaseTransactionException {

        switch (request.getMethod()) {
        case GET -> {
            Response response = getTradingDeals();
            transaction.commit();

            return response;
        }
        case POST -> {
            String body = request.getBody().toLowerCase();
            TradingDealDto dealDto = JsonUtils.getObjectFromJsonString(body, TradingDealDto.class);
            if (dealDto != null) {
                return createTradingDeal(user, dealDto);
            }
            return ResponseUtils.notImplemented();
        }
        default -> {
            return ResponseUtils.notImplemented();
        }
        }
    }

    private Response performCarryOutOrDealDeletion(Request request, User user)
            throws DatabaseTransactionException {
        UUID dealUuid = UUID.fromString(request.getPathParts().get(1));

        switch (request.getMethod()) {
        case POST -> {
            String body = request.getBody().toLowerCase();
            UUID cardUuid = JsonUtils.getObjectFromJsonString(body, UUID.class);
            return carryOutDeal(user, dealUuid, cardUuid);
        }
        case DELETE -> {
            return deleteTradingDeal(user, dealUuid);
        }
        default -> {
            return ResponseUtils.notImplemented();
        }
        }
    }

    /**
     * Gets the singleton instance of the {@code TradingController}.
     *
     * @return The singleton instance of the {@code TradingController}.
     */
    public static synchronized TradingController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TradingController();
        }
        return INSTANCE;
    }
}
