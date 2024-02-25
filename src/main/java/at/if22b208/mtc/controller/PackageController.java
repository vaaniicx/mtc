package at.if22b208.mtc.controller;

import java.util.List;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.database.Transaction;
import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import at.if22b208.mtc.util.mapper.CardMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling card package-related operations.
 */
@Slf4j
public class PackageController implements Controller {
    private static PackageController INSTANCE;

    private PackageController() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Creates a new card package based on the provided list of card data transfer objects (DTOs).
     * Each card in the list is converted to an entity and associated with the generated package UUID.
     * If a card with the same UUID already exists in the database, a conflict response is returned.
     *
     * @param cardDtoList The list of card DTOs to be included in the package.
     * @return A response indicating the success or failure of the package creation.
     */
    private Response createPackage(List<CardDto> cardDtoList) throws DatabaseTransactionException {
        int packageId = CardService.getInstance().getNextPackageId();

        for (CardDto dto : cardDtoList) {
            Card card = CardMapper.INSTANCE.map(dto);
            card.setPackageId(packageId);

            try {
                CardService.getInstance().create(card);
            } catch (InvalidPackageException e) {
                // Log the exception and return a conflict response.
                log.error("Error creating card package: {}", e.getMessage());
                return ResponseUtils.conflict(MessageConstants.PACKAGE_CARD_ALREADY_EXISTS);
            }
        }
        return ResponseUtils.created(MessageConstants.PACKAGE_CREATED);
    }

    /**
     * Handles incoming HTTP requests related to card packages. It validates the authorization,
     * checks for admin privileges, and processes POST requests for creating packages.
     *
     * @param request The incoming HTTP request to be handled.
     * @return A response indicating the result of processing the request.
     */
    @Override
    public Response handleRequest(Request request) {
        // Check if the request is authorized
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        // Validate admin privileges
        String token = SessionUtils.getBearerToken(request.getHeader());
        if (token == null || !token.startsWith("admin")) {
            return ResponseUtils.forbidden(MessageConstants.INSUFFICIENT_PRIVILEGE);
        }

        Transaction transaction = new Transaction();
        try {
            String root = request.getRoot();
            if (root.equalsIgnoreCase("packages")) {
                if (request.getMethod() == Method.POST) {
                    if (request.getPathParts().size() == 1) {
                        String body = request.getBody().toLowerCase();
                        List<CardDto> dtoList = JsonUtils.getListFromJsonString(body, CardDto.class);

                        Response response = createPackage(dtoList);
                        transaction.commit();

                        return response;
                    }
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
     * Gets the singleton instance of the {@code PackageController}.
     *
     * @return The singleton instance of the {@code PackageController}.
     */
    public static synchronized PackageController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackageController();
        }
        return INSTANCE;
    }
}
