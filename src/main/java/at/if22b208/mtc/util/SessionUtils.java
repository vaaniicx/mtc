package at.if22b208.mtc.util;

import at.if22b208.mtc.server.http.Header;
import at.if22b208.mtc.server.session.SessionManager;

/**
 * Utility class for working with user sessions and authorization.
 *
 * @see SessionManager
 */
public class SessionUtils {
    /**
     * Checks if the request is authorized based on the presence of a valid Bearer token in the authorization header.
     *
     * @param header The HTTP request header.
     * @return {@code true} if the request is authorized; {@code false} otherwise.
     */
    public static boolean isAuthorized(Header header) {
        String authorization = header.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ")
                && SessionManager.isValidSession(authorization);
    }

    /**
     * Retrieves the username from the Authorization header in the Bearer token.
     *
     * @param header The HTTP request header.
     * @return The username extracted from the Bearer token, or {@code null} if not present or invalid.
     */
    public static String getUsernameFromHeader(Header header) {
        String token = SessionUtils.getBearerToken(header);
        return SessionUtils.getUsernameFromUserSession(token);
    }

    /**
     * Retrieves the Bearer token from the Authorization header.
     *
     * @param header The HTTP request header.
     * @return The Bearer token, or {@code null} if not present or in an invalid format.
     */
    public static String getBearerToken(Header header) {
        String authorization = header.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    /**
     * Retrieves the username associated with the user session identified by the provided token.
     *
     * @param token The Bearer token.
     * @return The username associated with the user session, or {@code null} if the session is not valid.
     */
    private static String getUsernameFromUserSession(String token) {
        if (!SessionManager.isValidSession(token)) {
            return null;
        }
        return SessionManager.getUserSession(token).getUsername();
    }
}
