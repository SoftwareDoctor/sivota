package it.softwaredoctor.sivota.mapper;

import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.model.Domanda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


import java.util.List;


@Mapper(uses = {RispostaMapper.class, VotazioneMapper.class}, componentModel = "spring")
public interface DomandaMapper {
    @Mappings({
            @Mapping(target = "uuidDomanda", source = "uuidDomanda"),
            @Mapping(target = "testo", source = "testo"),
            @Mapping(target = "votazione", ignore = true),
            @Mapping(target = "risposte", source = "risposte")
    })
    DomandaDTO domandaToDomandaDTO(Domanda domanda);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuidDomanda", source = "uuidDomanda"),
            @Mapping(target = "testo", source = "testo"),
            @Mapping(target = "votazione", ignore = true),
            @Mapping(target = "risposte", source = "risposte")
    })
    Domanda domandaDTOToDomanda(DomandaDTO domandaDTO);

    List<DomandaDTO> domandeToDomandaDTOs(List<Domanda> domande);
    List<Domanda> domandaDTOsToDomande(List<DomandaDTO> domandaDTOs);

}
