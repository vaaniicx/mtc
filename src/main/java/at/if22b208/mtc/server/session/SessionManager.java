package at.if22b208.mtc.server.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Map<String, UserSession> sessionMap = new HashMap<>();

    public static String createSession(String username) {
        UserSession session = new UserSession(username, System.currentTimeMillis());
        String token = generateToken(username);
        sessionMap.put(token, session);
        return token;
    }

    public static boolean isValidSession(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        UserSession session = sessionMap.get(token);
        return session != null && !isSessionExpired(session);
    }

    private static boolean isSessionExpired(UserSession session) {
        long currentTime = System.currentTimeMillis();
        long sessionStartTime = session.getStartTime();
        long sessionTimeout = 30 * 60 * 1000; // = 30 minutes
        return currentTime - sessionStartTime > sessionTimeout;
    }

    public static UserSession getUserSession(String token) {
        return sessionMap.get(token);
    }

    public static void invalidateSession(String token) {
        sessionMap.remove(token);
    }

    private static String generateToken(String username) {
        return username + "-mtcgToken";
    }
}
