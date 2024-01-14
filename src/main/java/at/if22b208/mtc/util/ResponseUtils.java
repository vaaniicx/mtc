package at.if22b208.mtc.util;

import at.if22b208.mtc.config.MessageConstants;
import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.HttpStatus;
import at.if22b208.mtc.server.http.Response;

public class ResponseUtils {

    public static Response conflict(String message) {
        return new Response(HttpStatus.CONFLICT, ContentType.PLAIN_TEXT, message);
    }

    public static Response created(String message) {
        return new Response(HttpStatus.CREATED, ContentType.PLAIN_TEXT, message);
    }

    public static Response error(String message) {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, message);
    }

    public static Response forbidden(String message) {
        return new Response(HttpStatus.FORBIDDEN, ContentType.PLAIN_TEXT, message);
    }

    public static Response noContent(String message) {
        return new Response(HttpStatus.NO_CONTENT, ContentType.PLAIN_TEXT, message);
    }

    public static Response notFound(String message) {
        return new Response(HttpStatus.NOT_FOUND, ContentType.PLAIN_TEXT, message);
    }

    public static Response notImplemented() {
        return new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.PLAIN_TEXT, MessageConstants.NOT_IMPLEMENTED);
    }

    public static Response ok(ContentType type, String message) {
        return new Response(HttpStatus.OK, type, message);
    }

    public static Response unauthorized() {
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, MessageConstants.UNAUTHORIZED);
    }

    public static Response unauthorized(String message) {
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, message);
    }
}
