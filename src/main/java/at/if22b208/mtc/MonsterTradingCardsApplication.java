package at.if22b208.mtc;

import at.if22b208.mtc.controller.*;
import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.server.Server;
import at.if22b208.mtc.server.util.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonsterTradingCardsApplication {

    /**
     * Starts the REST-Server listening on port 10001
     *
     * @param args args
     */
    public static void main(String[] args) {
        Database.getINSTANCE().connect();
        if (!Database.getINSTANCE().isConnected()) {
            log.error("Not connected to database, shut down application.");
            return;
        }

        Server server = new Server(10001, configureRouter());
        server.start();
    }

    private static Router configureRouter() {
        Router router = new Router();
        router.addController("/users", UserController.getInstance());
        router.addController("/sessions", SessionController.getInstance());
        router.addController("/packages", PackageController.getInstance());
        router.addController("/transactions", TransactionController.getInstance());
        router.addController("/cards", CardController.getInstance());
        router.addController("/deck", CardController.getInstance());
        router.addController("/stats", null);
        router.addController("/scoreboard", null);
        router.addController("/battles", BattleController.getInstance());
        router.addController("/tradings", null);
        return router;
    }
}