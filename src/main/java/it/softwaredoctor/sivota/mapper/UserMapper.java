package it.softwaredoctor.sivota.mapper;

import it.softwaredoctor.sivota.dto.UserDTO;
import it.softwaredoctor.sivota.dto.UserLoginDTO;
import it.softwaredoctor.sivota.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {VotazioneMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuidUser", ignore = true),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "surname", source = "surname"),
            @Mapping(target = "votazione", source = "votazione"),
            @Mapping(target = "roleName", ignore = true),
            @Mapping(target = "registrato", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "verificationToken", ignore = true),
            @Mapping(target = "tokenCreationDate", ignore = true)
    })

    User userDTOToUser(UserDTO userDTO);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuidUser", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "surname", ignore = true),
            @Mapping(target = "votazione", ignore = true),
            @Mapping(target = "roleName", ignore = true),
            @Mapping(target = "registrato", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "verificationToken", ignore = true),
            @Mapping(target = "tokenCreationDate", ignore = true)
    })
    User userLoginDTOToUser(UserLoginDTO userLoginDTO);

}
