package at.if22b208.mtc.util;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.HttpStatus;
import at.if22b208.mtc.server.http.Response;

/**
 * Utility class for creating common HTTP responses.
 */
public class ResponseUtils {
    /**
     * Creates a response indicating a conflict (HTTP status 409).
     *
     * @param message The message to include in the response body.
     * @return A response indicating a conflict.
     */
    public static Response conflict(String message) {
        return new Response(HttpStatus.CONFLICT, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating a bad request (HTTP status 400).
     *
     * @param message The message to include in the response body.
     * @return A response indicating a bad request.
     */
    public static Response badRequest(String message) {
        return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating a successful creation (HTTP status 201).
     *
     * @param message The message to include in the response body.
     * @return A response indicating a successful creation.
     */
    public static Response created(String message) {
        return new Response(HttpStatus.CREATED, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating an internal server error (HTTP status 500).
     *
     * @param message The message to include in the response body.
     * @return A response indicating an internal server error.
     */
    public static Response error(String message) {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating forbidden access (HTTP status 403).
     *
     * @param message The message to include in the response body.
     * @return A response indicating forbidden access.
     */
    public static Response forbidden(String message) {
        return new Response(HttpStatus.FORBIDDEN, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating forbidden access (HTTP status 403).
     *
     * @param message The message to include in the response body.
     * @return A response indicating forbidden access.
     */
    public static Response noContent(String message) {
        return new Response(HttpStatus.NO_CONTENT, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating not found (HTTP status 404).
     *
     * @param message The message to include in the response body.
     * @return A response indicating not found.
     */
    public static Response notFound(String message) {
        return new Response(HttpStatus.NOT_FOUND, ContentType.PLAIN_TEXT, message);
    }

    /**
     * Creates a response indicating not implemented (HTTP status 501).
     *
     * @return A response indicating not implemented.
     */
    public static Response notImplemented() {
        return new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.PLAIN_TEXT, MessageConstants.NOT_IMPLEMENTED);
    }

    /**
     * Creates a response indicating success with an OK status (HTTP status 200).
     *
     * @param type    The content type of the response body.
     * @param message The message to include in the response body.
     * @return A response indicating success.
     */
    public static Response ok(ContentType type, String message) {
        return new Response(HttpStatus.OK, type, message);
    }

    /**
     * Creates a response indicating unauthorized access (HTTP status 401).
     *
     * @return A response indicating unauthorized access.
     */
    public static Response unauthorized() {
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, MessageConstants.UNAUTHORIZED);
    }

    /**
     * Creates a response indicating unauthorized access with a custom message (HTTP status 401).
     *
     * @param message The message to include in the response body.
     * @return A response indicating unauthorized access with a custom message.
     */
    public static Response unauthorized(String message) {
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, message);
    }
}
