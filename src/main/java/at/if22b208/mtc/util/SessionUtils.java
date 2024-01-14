package at.if22b208.mtc.util;

import at.if22b208.mtc.server.http.Header;
import at.if22b208.mtc.server.session.SessionManager;

public class SessionUtils {
    public static boolean isAuthorized(Header header) {
        String authorization = header.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ") && SessionManager.isValidSession(authorization);
    }

    public static String getBearerToken(Header header) {
        String authorization = header.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    public static String getUsernameFromUserSession(String token) {
        if (!SessionManager.isValidSession(token)) {
            return null;
        }
        return SessionManager.getUserSession(token).getUsername();
    }
}
