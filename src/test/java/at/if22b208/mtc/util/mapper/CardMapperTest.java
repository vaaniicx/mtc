package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardMapperTest {

    private final CardMapper mapper = new CardMapperImpl();

    @Test
    @DisplayName("Mapping empty Card to CardDto")
    void test_mapEmptyFields() {
        Card card = Card.builder().build();

        assertDoesNotThrow(() -> mapper.map(card));

        CardDto dto = mapper.map(card);
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(card.getUuid(), dto.uuid()),
                () -> assertEquals(card.getName(), dto.name()),
                () -> assertEquals(card.getDamage(), dto.damage())
        );
    }

    @Test
    @DisplayName("Mapping Card to CardDto")
    void test_mapToDto() {
        Card card = Card.builder()
                .uuid(UUID.randomUUID())
                .name("test")
                .damage(10)
                .build();

        CardDto dto = mapper.map(card);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(card.getUuid(), dto.uuid()),
                () -> assertEquals(card.getName(), dto.name()),
                () -> assertEquals(card.getDamage(), dto.damage())
        );
    }
}
