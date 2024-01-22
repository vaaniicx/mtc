package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {
    private final CardMapper mapper = new CardMapperImpl();

    @Test
    @DisplayName("Mapping empty Card to CardDto")
    void test_mapEmptyCardToDto() {
        Card card = Card.builder().build();
        assertDoesNotThrow(() -> mapper.map(card));
        CardDto dto = mapper.map(card);
        assertionsOnMapping(dto, card);
    }

    @Test
    @DisplayName("Mapping empty CardDto to Card")
    void test_mapEmptyCardDtoToCard() {
        CardDto dto = new CardDto();
        assertDoesNotThrow(() -> mapper.map(dto));
        Card card = mapper.map(dto);
        assertionsOnMapping(dto, card);
    }

    @Test
    @DisplayName("Mapping Card to CardDto")
    void test_mapCardToCardDto() {
        Card card = Card.builder()
                .uuid(UUID.randomUUID())
                .name("test")
                .damage(10)
                .build();

        CardDto dto = mapper.map(card);
        assertionsOnMapping(dto, card);
    }

    @Test
    @DisplayName("Mapping CardDto to Card")
    void test_mapCardDtoToCard() {
        CardDto dto = new CardDto();
        dto.setUuid(UUID.randomUUID());
        dto.setName("test");
        dto.setDamage(10.0);

        Card card = mapper.map(dto);
        assertionsOnMapping(dto, card);
    }

    private static void assertionsOnMapping(CardDto dto, Card card) {
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(card.getUuid(), dto.getUuid()),
                () -> assertEquals(card.getName(), dto.getName()),
                () -> assertEquals(card.getDamage(), dto.getDamage())
        );
    }
}
