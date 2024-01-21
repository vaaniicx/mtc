package at.if22b208.mtc.controller;

import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BattleController implements Controller {
    private static BattleController INSTANCE;

    private BattleController() {
        // hide constructor
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        return null;
    }

    public static synchronized BattleController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleController();
        }
        return INSTANCE;
    }
}
