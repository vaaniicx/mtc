package at.if22b208.mtc.controller;

import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import at.if22b208.mtc.service.UserService;
import at.if22b208.mtc.util.ResponseUtils;
import at.if22b208.mtc.util.SessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.atomic.AtomicReference;

public class BattleController implements Controller {
    private static BattleController INSTANCE;
    private AtomicReference<User> lobby;

    private BattleController() {
        // hide constructor
    }

    private Response enterBattleLobby(String username) {
        User user = UserService.getInstance().getByUsername(username);
        return ResponseUtils.notImplemented();
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        if (!SessionUtils.isAuthorized(request.getHeader())) {
            return ResponseUtils.unauthorized();
        }

        String root = request.getRoot();
        if (root.equalsIgnoreCase("battles")) {
            if (request.getMethod() == Method.POST) {
                // Retrieve the username from the user session
                String username = SessionUtils.getUsernameFromHeader(request.getHeader());
                return this.enterBattleLobby(username);
            }
        }
        return ResponseUtils.notImplemented();
    }

    public static synchronized BattleController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleController();
        }
        return INSTANCE;
    }
}
