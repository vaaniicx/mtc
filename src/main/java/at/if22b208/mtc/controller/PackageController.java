package at.if22b208.mtc.controller;

import at.if22b208.mtc.server.Controller;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PackageController implements Controller {
    private static PackageController INSTANCE;

    private PackageController() {
    }

    public static synchronized PackageController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PackageController();
        }
        return INSTANCE;
    }

    @Override
    public Response handleRequest(Request request) throws JsonProcessingException {
        return null;
    }
}
