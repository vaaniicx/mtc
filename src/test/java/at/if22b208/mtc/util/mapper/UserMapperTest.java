package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.user.UserDataDto;
import at.if22b208.mtc.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @InjectMocks
    private UserMapperImpl mapper;

    @Test
    @DisplayName("Mapping empty User to UserDataDto")
    void test_mapEmptyFields() {
        User user = User.builder().build();

        assertDoesNotThrow(() -> mapper.mapToUserDataDto(user));

        UserDataDto dto = mapper.mapToUserDataDto(user);
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(user.getName(), dto.getName()),
                () -> assertEquals(user.getName(), dto.getBiography()),
                () -> assertEquals(user.getImage(), dto.getImage())
        );
    }

    @Test
    @DisplayName("Mapping User to UserDataDto")
    void test_mapToDto() {
        User user = User.builder().build();

        UserDataDto dto = mapper.mapToUserDataDto(user);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(user.getName(), dto.getName()),
                () -> assertEquals(user.getBiography(), dto.getBiography()),
                () -> assertEquals(user.getImage(), dto.getImage())
        );
    }
}
