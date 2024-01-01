package at.if22b208.mtc.controller;

import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CardController implements Controller {
    private static CardController INSTANCE;

    private CardController() {
    }

    public static synchronized CardController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardController();
        }
        return INSTANCE;
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        return null;
    }
}
