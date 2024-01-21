package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.Battle;
import at.if22b208.mtc.entity.User;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BattleService {
    private static BattleService INSTANCE;
    private final BlockingQueue<Battle> battles;

    private BattleService() {
        // hide constructor
        this.battles = new LinkedBlockingQueue<>();
    }

    public Battle enterBattleQueue(User user) throws InterruptedException {
        if (battles.isEmpty()) {
            Battle battle = new Battle();
            battle.setPlayerA(user);
            battles.add(battle);

            battle.getLatch().await();
            return battle;
        }

        Battle battleFromQueue = battles.take();
        battleFromQueue.setPlayerB(user);

        battleFromQueue.battle();

        return battleFromQueue;
    }

    public static synchronized BattleService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleService();
        }
        return INSTANCE;
    }
}
