package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.user.UserDataDto;
import at.if22b208.mtc.dto.user.UserStatsDto;
import at.if22b208.mtc.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private final UserMapper mapper = new UserMapperImpl();

    @Test
    @DisplayName("Mapping empty User")
    void test_mapEmptyFields() {
        User user = User.builder().build();

        assertDoesNotThrow(() -> mapper.mapToUserDataDto(user));
        assertDoesNotThrow(() -> mapper.mapToUserStatsDto(user));

        UserDataDto userDataDto = mapper.mapToUserDataDto(user);
        assertionsOnUserDataDto(user, userDataDto);

        UserStatsDto userStatsDto = mapper.mapToUserStatsDto(user);
        assertionsOnUserStatsDto(user, userStatsDto);
    }

    @Test
    @DisplayName("Mapping User to UserDataDto")
    void test_mapToUserDataDto() {
        User user = User.builder().build();
        UserDataDto dto = mapper.mapToUserDataDto(user);
        assertionsOnUserDataDto(user, dto);
    }

    @Test
    @DisplayName("Mapping User to UserStatsDto")
    void test_mapToUserStatsDto() {
        User user = User.builder().build();
        UserStatsDto dto = mapper.mapToUserStatsDto(user);
        assertionsOnUserStatsDto(user, dto);
    }

    private void assertionsOnUserDataDto(User expected, UserDataDto actual) {
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getName(), actual.getBiography()),
                () -> assertEquals(expected.getImage(), actual.getImage())
        );
    }

    private void assertionsOnUserStatsDto(User expected, UserStatsDto actual) {
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getName(), actual.name()),
                () -> assertEquals(expected.getElo(), actual.elo()),
                () -> assertEquals(expected.getWins(), actual.wins()),
                () -> assertEquals(expected.getLosses(), actual.losses())
        );
    }
}
