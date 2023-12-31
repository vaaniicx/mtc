package at.if22b208.mtc;

import at.if22b208.mtc.server.Server;
import at.if22b208.mtc.server.util.Router;
import at.if22b208.mtc.service.UserService;

public class StarterClass {

    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        server.start();
    }

    private static Router configureRouter() {
        Router router = new Router();
        router.addService("/user", UserService.getInstance());

        return router;
    }
}