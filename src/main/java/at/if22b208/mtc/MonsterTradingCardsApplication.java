package at.if22b208.mtc;

import at.if22b208.mtc.controller.*;
import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.server.Server;
import at.if22b208.mtc.server.util.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * The main class for the Monster Trading Cards application.
 * Initializes the database connection and starts the REST server.
 */
@Slf4j
public class MonsterTradingCardsApplication {

    /**
     * Starts the REST-Server listening on port 10001
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        Database.getInstance().connect();
        if (!Database.getInstance().isConnected()) {
            log.error("Not connected to database, shut down application.");
            return;
        }

        Server server = new Server(10001, configureRouter());
        server.start();
    }

    /**
     * Configures the router by adding controllers for different endpoints.
     *
     * @return The configured router.
     */
    private static Router configureRouter() {
        Router router = new Router();
        router.addController("/users", UserController.getInstance());
        router.addController("/sessions", SessionController.getInstance());
        router.addController("/packages", PackageController.getInstance());
        router.addController("/transactions", TransactionController.getInstance());
        router.addController("/cards", CardController.getInstance());
        router.addController("/deck", DeckController.getInstance());
        router.addController("/stats", StatisticController.getInstance());
        router.addController("/scoreboard", ScoreboardController.getInstance());
        router.addController("/battles", BattleController.getInstance());
        router.addController("/tradings", TradingController.getInstance());
        return router;
    }
}