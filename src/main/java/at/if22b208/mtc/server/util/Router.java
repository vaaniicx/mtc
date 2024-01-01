package at.if22b208.mtc.server.util;

import at.if22b208.mtc.server.Controller;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> serviceRegistry = new HashMap<>();

    public void addController(String route, Controller controller) {
        this.serviceRegistry.put(route, controller);
    }

    public void removeService(String route) {
        this.serviceRegistry.remove(route);
    }

    public Controller resolve(String route) {
        return this.serviceRegistry.get(route);
    }

}
