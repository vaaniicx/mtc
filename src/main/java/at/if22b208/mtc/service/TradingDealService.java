package at.if22b208.mtc.service;

import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.exception.InvalidTradingDealException;
import at.if22b208.mtc.repository.TradingDealRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradingDealService implements Service<TradingDeal, UUID> {
    private static TradingDealService INSTANCE;

    private TradingDealService() {
        // Private constructor to ensure singleton pattern.
    }

    @Override
    public TradingDeal create(TradingDeal deal) throws InvalidTradingDealException {
        if (this.getById(deal.getUuid()) != null) {
            throw new InvalidTradingDealException("Trading deal already exists.");
        }
        return TradingDealRepository.getInstance().create(deal);
    }

    @Override
    public List<TradingDeal> getAll() {
        List<Optional<TradingDeal>> all = TradingDealRepository.getInstance().findAll();
        return all.stream()
                .map(deal -> deal.orElse(null))
                .toList();
    }

    @Override
    public TradingDeal getById(UUID uuid) {
        return TradingDealRepository.getInstance()
                .findById(uuid)
                .orElse(null);
    }

    public void deleteById(UUID uuid) {
        TradingDealRepository.getInstance().delete(uuid);
    }

    public static synchronized TradingDealService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TradingDealService();
        }
        return INSTANCE;
    }
}
