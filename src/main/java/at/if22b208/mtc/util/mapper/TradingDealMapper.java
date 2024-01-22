package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.trading.TradingDealDto;
import at.if22b208.mtc.entity.TradingDeal;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The {@code TradingDealMapper} interface is used with MapStruct to automatically generate
 * mapping code between {@link TradingDeal} and {@link TradingDealDto} instances.
 * It defines a method for mapping a {@link TradingDealDto} to a {@link TradingDeal}.
 * The implementation of this interface is generated by MapStruct at compile time.
 *
 * <p>Usage:</p>
 * <pre>
 * {@code
 * TradingDeal tradingDeal = TradingDealMapper.INSTANCE.map(tradingDealDto);
 * }
 * </pre>
 *
 * @see Mapper
 * @see Mappers
 */
@Mapper
public interface TradingDealMapper {
    TradingDealMapper INSTANCE = Mappers.getMapper(TradingDealMapper.class);

    /**
     * Maps a {@link TradingDealDto} to a {@link TradingDeal}.
     *
     * @param deal The {@link TradingDealDto} to be mapped.
     * @return The corresponding {@link TradingDeal}.
     */
    TradingDeal map(TradingDealDto deal);
}
