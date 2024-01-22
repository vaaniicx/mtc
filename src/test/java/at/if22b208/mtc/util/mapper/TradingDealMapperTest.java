package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.trading.TradingDealDto;
import at.if22b208.mtc.entity.TradingDeal;
import at.if22b208.mtc.entity.enumeration.CardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TradingDealMapper} implementations.
 * These tests cover the functionality of mapping {@link TradingDealDto} to {@link TradingDeal}.
 */
@ExtendWith(MockitoExtension.class)
public class TradingDealMapperTest {
    private final TradingDealMapper mapper = new TradingDealMapperImpl();

    @Test
    @DisplayName("Mapping empty TradingDealDto to TradingDeal")
    void test_mapEmptyTradingDealDtoToTradingDeal() {
        TradingDealDto dealDto = TradingDealDto.builder().build();
        assertDoesNotThrow(() -> mapper.map(dealDto));
        assertionsOnMapping(dealDto, mapper.map(dealDto));
    }

    @Test
    @DisplayName("Mapping TradingDealDto to TradingDeal")
    void test_mapTradingDealDtoToTradingDeal() {
        TradingDealDto dealDto = TradingDealDto.builder()
                .uuid(UUID.randomUUID())
                .cardUuid(UUID.randomUUID())
                .cardType(CardType.MONSTER)
                .minimumDamage(12.0)
                .build();
        assertionsOnMapping(dealDto, mapper.map(dealDto));
    }

    private static void assertionsOnMapping(TradingDealDto dealDto, TradingDeal deal) {
        assertAll(
                () -> assertNotNull(deal),
                () -> assertEquals(dealDto.getUuid(), deal.getUuid()),
                () -> assertEquals(dealDto.getCardUuid(), deal.getCardUuid()),
                () -> assertEquals(dealDto.getCardType(), deal.getCardType()),
                () -> assertEquals(dealDto.getMinimumDamage(), deal.getMinimumDamage())
        );
    }
}
