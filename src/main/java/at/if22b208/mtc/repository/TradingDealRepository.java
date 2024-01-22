package at.if22b208.mtc.repository;

import at.if22b208.mtc.database.Database;
import at.if22b208.mtc.database.Result;
import at.if22b208.mtc.database.Row;
import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.entity.enumeration.CardType;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradingDealRepository implements Repository<TradingDeal, UUID> {
    private static TradingDealRepository INSTANCE;
    private static final String TABLE = "trading_deal";

    private TradingDealRepository() {
        // hide constructor
    }

    /**
     * Retrieves all trading deals from the database.
     *
     * @return A list of all trading deals in the database.
     */
    @Override
    public List<Optional<TradingDeal>> findAll() {
        String query = "SELECT uuid, card_uuid, card_type, card_damage FROM " + SCHEMA + TABLE;
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query);

        List<Optional<TradingDeal>> deals = new ArrayList<>();
        for (Row row : result.getRows()) {
            deals.add(Optional.of(buildTradingDealFromRow(row)));
        }
        return deals;
    }

    @Override
    public Optional<TradingDeal> findById(UUID uuid) {
        String query = "SELECT uuid, card_uuid, card_type, card_damage FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        Result result = database.executeSelectQuery(query, uuid);

        for (Row row : result.getRows()) {
            return Optional.of(buildTradingDealFromRow(row));
        }
        return Optional.empty();
    }

    @Override
    public TradingDeal create(TradingDeal deal) {
        String query = "INSERT INTO " + SCHEMA + TABLE + " (uuid, card_uuid, card_type, card_damage)" +
                " VALUES (?, ?, ?, ?)";
        val database = Database.getInstance();
        database.executeInsertQuery(query,
                deal.getUuid(), deal.getCardUuid(), deal.getCardType().name(), deal.getMinimumDamage());
        return deal;
    }

    public void delete(UUID uuid) {
        String query = "DELETE FROM " + SCHEMA + TABLE + " WHERE uuid = ?";
        val database = Database.getInstance();
        database.executeUpdateQuery(query, uuid);
    }

    private TradingDeal buildTradingDealFromRow(Row row) {
        return TradingDeal.builder()
                .uuid(row.getUuid("uuid"))
                .cardUuid(row.getUuid("card_uuid"))
                .cardType(CardType.valueOf(row.getString("card_type")))
                .minimumDamage(row.getDouble("card_damage"))
                .build();
    }

    public static synchronized TradingDealRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TradingDealRepository();
        }
        return INSTANCE;
    }
}
