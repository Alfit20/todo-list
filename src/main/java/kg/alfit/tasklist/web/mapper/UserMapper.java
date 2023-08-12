package kg.alfit.tasklist.web.mapper;

import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.web.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
