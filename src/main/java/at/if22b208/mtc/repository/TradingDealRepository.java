package at.if22b208.mtc.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.database.Result;
import at.if22b208.mtc.database.Row;
import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.entity.enumeration.CardType;
import at.if22b208.mtc.exception.DatabaseTransactionException;
import lombok.val;

/**
 * The {@code TradingDealRepository} class is responsible for handling database operations related to trading deals.
 *
 * <p>This repository provides methods for finding, creating, and deleting trading deals from the database.</p>
 *
 * @see Repository
 * @see TradingDeal
 */
public class TradingDealRepository implements Repository<TradingDeal, UUID> {
    private static TradingDealRepository INSTANCE;
    private static final String TABLE = "trading_deal";

    private TradingDealRepository() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Retrieves all trading deals from the database.
     *
     * @return A list of all trading deals in the database.
     */
    @Override
    public List<Optional<TradingDeal>> findAll() throws DatabaseTransactionException {
        String query = "SELECT uuid, card_uuid, card_type, card_damage FROM " + SCHEMA + TABLE;
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);

        List<Optional<TradingDeal>> deals = new ArrayList<>();
        for (Row row : result.getRows()) {
            deals.add(Optional.of(buildTradingDealFromRow(row)));
        }
        return deals;
    }

    /**
     * Finds a trading deal by its UUID in the database.
     *
     * @param uuid The UUID of the trading deal to find.
     * @return An Optional containing the found trading deal, or an empty Optional if not found.
     */
    @Override
    public Optional<TradingDeal> findById(UUID uuid) throws DatabaseTransactionException {
        String query = "SELECT uuid, card_uuid, card_type, card_damage FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query, uuid);

        for (Row row : result.getRows()) {
            return Optional.of(buildTradingDealFromRow(row));
        }
        return Optional.empty();
    }

    /**
     * Creates a new trading deal in the database.
     *
     * @param deal The trading deal to be created.
     * @return The created trading deal.
     */
    @Override
    public TradingDeal create(TradingDeal deal) throws DatabaseTransactionException {
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, card_uuid, card_type, card_damage)" +
                " VALUES (?, ?, ?, ?)";
        val database = Database.getInstance();
        database.executeInsertQuery(query,
                deal.getUuid(), deal.getCardUuid(), deal.getCardType().name(), deal.getMinimumDamage());
        return deal;
    }

    /**
     * Deletes a trading deal from the database by its UUID.
     *
     * @param uuid The UUID of the trading deal to be deleted.
     */
    public void delete(UUID uuid) throws DatabaseTransactionException {
        String query = "DELETE FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, uuid);
    }

    /**
     * Builds a TradingDeal entity from a database row.
     *
     * @param row The database row containing trading deal data.
     * @return A TradingDeal entity built from the database row.
     */
    private TradingDeal buildTradingDealFromRow(Row row) {
        return TradingDeal.builder()
                .uuid(row.getUuid("uuid"))
                .cardUuid(row.getUuid("card_uuid"))
                .cardType(CardType.valueOf(row.getString("card_type")))
                .minimumDamage(row.getDouble("card_damage"))
                .build();
    }

    /**
     * Gets a singleton instance of the TradingDealRepository.
     *
     * @return The singleton instance of the TradingDealRepository.
     */
    public static synchronized TradingDealRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TradingDealRepository();
        }
        return INSTANCE;
    }
}
