package at.if22b208.mtc.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.BalanceTransactionException;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.NameNotValidException;
import at.if22b208.mtc.exception.NegativeBalanceException;
import at.if22b208.mtc.repository.UserRepository;
import at.if22b208.mtc.util.balance.BalanceOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService implements Service<User, UUID> {
    private static UserService INSTANCE;

    private UserService() {
        // Private constructor to ensure singleton pattern.
    }

    public List<User> getAll() throws DatabaseTransactionException {
        List<Optional<User>> all = UserRepository.getInstance().findAll();
        return all.stream()
                .map(user -> user.orElse(null))
                .toList();
    }

    @Override
    public User getById(UUID uuid) throws DatabaseTransactionException {
        return UserRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    public User getByUsername(String username) throws DatabaseTransactionException {
        return UserRepository.getInstance()
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public User create(User user) throws DatabaseTransactionException {
        if (getByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setBalance(BigInteger.valueOf(20));
        return UserRepository.getInstance().create(user);
    }

    /**
     * Updates the balance of a user based on the provided amount and operation.
     *
     * @param user      The user whose balance needs to be updated.
     * @param amount    The amount to update the balance by.
     * @param operation The operation to perform on the balance.
     */
    public void updateBalance(User user, BigInteger amount, BalanceOperation operation) throws BalanceTransactionException {
        try {
            BigInteger newBalance = operation.operate(user.getBalance(), amount);
            user.setBalance(newBalance);
            UserRepository.getInstance().updateBalance(user);
        } catch (NegativeBalanceException | DatabaseTransactionException e) {
            log.warn("Error occurred during balance transaction. No operation performed.", e);
            throw new BalanceTransactionException("Error occurred during balance transaction. No operation performed.");
        }
    }

    public void updateElo(User user) throws DatabaseTransactionException {
        UserRepository.getInstance().updateElo(user);
    }

    public void updateLoss(User user, BigInteger amount, BalanceOperation operation) throws BalanceTransactionException {
        try {
            BigInteger newLosses = operation.operate(BigInteger.valueOf(user.getLosses()), amount);
            user.setLosses(newLosses.intValue());
            UserRepository.getInstance().updateLoss(user);
        } catch (NegativeBalanceException | DatabaseTransactionException e) {
            log.warn("Error occurred during losses update. No operation performed.", e);
            throw new BalanceTransactionException("Error occurred during losses update. No operation performed.");
        }
    }

    public void updateWin(User user, BigInteger amount, BalanceOperation operation) throws BalanceTransactionException {
        try {
            BigInteger newWins = operation.operate(BigInteger.valueOf(user.getWins()), amount);
            user.setWins(newWins.intValue());
            UserRepository.getInstance().updateWin(user);
        } catch (NegativeBalanceException | DatabaseTransactionException e) {
            log.warn("Error occurred during losses update. No operation performed.", e);
            throw new BalanceTransactionException("Error occurred during losses update. No operation performed.");
        }
    }

    public List<Card> getDeckByOwner(User user) throws DatabaseTransactionException {
        Optional<User> optional = UserRepository.getInstance().findByUsername(user.getUsername());
        return optional.map(User::getDeck).orElse(new ArrayList<>());
    }

    public void updateDeck(User user) throws DatabaseTransactionException {
        UserRepository.getInstance().updateDeck(user);
    }

    public void updateUserData(User user) throws NameNotValidException, DatabaseTransactionException {
        if (!user.getName().startsWith(user.getUsername())) {
            throw new NameNotValidException("Name does not start with username.");
        }
        UserRepository.getInstance().updateUserData(user);
    }

    public static synchronized UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return INSTANCE;
    }
}

