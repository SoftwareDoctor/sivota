package it.softwaredoctor.sivota.mapper;

import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.Domanda;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.model.Votazione;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


//    default List<VotazioneDTO> votazioneListToVotazioneDTOList(List<Votazione> votazioni) {
//        return votazioni.stream()
//                .map(this::votazioneToVotazioneDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Mapping(target = "titolo", source = "titolo")
//    @Mapping(target = "votantiEmail", source = "votantiEmail")
//    @Mapping(target = "dataCreazione", source = "dataCreazione")
//    VotazioneDTO votazioneToVotazioneDTO(Votazione votazione);

    List <VotazioneDTO> votazioneListToVotazioneDTOList(List<Votazione> votazioni);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuidVotazione", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateVotazioneFromDTO(VotazioneDTO dto, @MappingTarget Votazione entity);

}
