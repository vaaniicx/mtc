package at.if22b208.mtc.controller;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.CardService;
import at.if22b208.mtc.util.JsonUtils;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PackageController implements Controller {
    private static PackageController INSTANCE;

    private PackageController() {
        // hide constructor
    }

    /**
     * Creates a new card package based on the provided list of card data transfer objects (DTOs).
     * Each card in the list is converted to an entity and associated with the generated package UUID.
     * If a card with the same UUID already exists in the database, a conflict response is returned.
     *
     * @param dtoList The list of card DTOs to be included in the package.
     * @return A response indicating the success or failure of the package creation.
     */
    private Response createPackage(List<CardDto> dtoList) {
        int packageId = CardService.getInstance().getNextPackageId();

        for (CardDto dto : dtoList) {
            Card card = Card.builder()
                    .uuid(dto.uuid())
                    .name(dto.name())
                    .damage(dto.damage())
                    .packageId(packageId)
                    .build();

            try {
                CardService.getInstance().create(card);
            } catch (InvalidPackageException e) {
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
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String token = SessionUtils.getBearerToken(request.getHeader());
        if (token == null || !token.startsWith("admin")) {
            return ResponseUtils.forbidden(MessageConstants.INSUFFICIENT_PRIVILEGE);
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("packages")) {
            if (request.getMethod() == Method.POST) {
                if (request.getPathParts().size() == 1) {
                    String body = request.getBody().toLowerCase();
                    List<CardDto> dtoList = JsonUtils.getListFromJsonString(body, CardDto.class);
                    return createPackage(dtoList);
                }
            }
        }
        return null;
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
