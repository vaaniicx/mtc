package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.Card;
import at.if22b208.mtc.entity.User;
import at.if22b208.mtc.exception.BalanceTransactionException;
import at.if22b208.mtc.exception.NegativeBalanceException;
import at.if22b208.mtc.repository.UserRepository;
import at.if22b208.mtc.util.balance.BalanceOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UserService implements Service<User, UUID> {
    private static UserService INSTANCE;

    private UserService() {
    }

    public List<User> getAll() {
        return UserRepository.getInstance().findAll();
    }

    @Override
    public User getById(UUID uuid) {
        return UserRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    public User getByUsername(String username) {
        return UserRepository.getInstance()
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public User create(User user) {
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
        } catch (NegativeBalanceException e) {
            log.warn("Error occurred during balance transaction. No operation performed.", e);
            throw new BalanceTransactionException("Error occurred during balance transaction. No operation performed.");
        }
    }

    public List<Card> getDeckByOwner(User user) throws JsonProcessingException {
        return UserRepository.getInstance()
                .getDeck(user)
                .stream()
                .map(CardService.getInstance()::getById)
                .toList();
    }

    public void updateDeck(User user) throws JsonProcessingException {
        UserRepository.getInstance().updateDeck(user);
    }

    public static synchronized UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return INSTANCE;
    }
}
