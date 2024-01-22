package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.Battle;
import at.if22b208.mtc.entity.User;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@code BattleService} class manages the battle queue and facilitates the creation of battles between users.
 *
 * <p>This service class provides methods for entering the battle queue and handling the initiation of battles.</p>
 */
public class BattleService {
    private static BattleService INSTANCE;
    private final BlockingQueue<Battle> battles;

    private BattleService() {
        // Private constructor to ensure singleton pattern.
        this.battles = new LinkedBlockingQueue<>();
    }

    /**
     * Enters the battle queue with the specified user.
     *
     * <p>If the queue is empty, a new battle is created with the user as Player A.
     * If the queue is not empty, the user is paired with the battle at the front of the queue as Player B.</p>
     *
     * @param user The user entering the battle queue.
     * @return The created or joined battle.
     * @throws InterruptedException If the thread is interrupted while waiting for a battle.
     */
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

    /**
     * Gets the singleton instance of the {@code BattleService}.
     *
     * @return The singleton instance of the {@code BattleService}.
     */
    public static synchronized BattleService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BattleService();
        }
        return INSTANCE;
    }
}
