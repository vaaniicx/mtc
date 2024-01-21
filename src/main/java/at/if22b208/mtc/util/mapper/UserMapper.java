package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.user.UserDataDto;
import at.if22b208.mtc.dto.user.UserStatsDto;
import at.if22b208.mtc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDataDto mapToUserDataDto(User user);

    @Mapping(source = "username", target="name")
    UserStatsDto mapToUserStatsDto(User user);
}
