package at.if22b208.mtc.service;

public class BattleService {
    private static BattleService INSTANCE;

    private BattleService() {
        // hide constructor
    }

    public static synchronized BattleService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleService();
        }
        return INSTANCE;
    }
}
