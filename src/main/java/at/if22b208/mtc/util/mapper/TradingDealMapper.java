package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.trading.TradingDealDto;
import at.if22b208.mtc.entity.TradingDeal;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TradingDealMapper {
    TradingDealMapper INSTANCE = Mappers.getMapper(TradingDealMapper.class);

    TradingDeal map(TradingDealDto deal);
}
