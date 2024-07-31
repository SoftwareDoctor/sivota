package it.softwaredoctor.sivota.mapper;

import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.Votazione;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DomandaMapper.class, UserMapper.class})
public interface VotazioneMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuidVotazione", source = "uuidVotazione"),
            @Mapping(target = "titolo", source = "titolo"),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "domande", source = "domande"),
            @Mapping(target = "votantiEmail", source = "votantiEmail"),
            @Mapping(target = "dataCreazione", source = "dataCreazione"),
            @Mapping(target = "isAnonymous", source = "isAnonymous")
    })
    Votazione votazioneDTOToVotazione(VotazioneDTO votazioneDTO);


    @Mappings({
            @Mapping(target = "uuidVotazione", source = "uuidVotazione"),
            @Mapping(target = "titolo", source = "titolo"),
            @Mapping(target = "domande", source = "domande"),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "votantiEmail", source = "votantiEmail"),
            @Mapping(target = "dataCreazione", source = "dataCreazione"),
            @Mapping(target = "isAnonymous", source = "isAnonymous")
    })
    VotazioneDTO votazioneToVotazioneDto(Votazione votazione);

    List <VotazioneDTO> votazioneListToVotazioneDTOList(List<Votazione> votazioni);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuidVotazione", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateVotazioneFromDTO(VotazioneDTO dto, @MappingTarget Votazione entity);

}
