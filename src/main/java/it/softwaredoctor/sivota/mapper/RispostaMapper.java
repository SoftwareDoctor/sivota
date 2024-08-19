package it.softwaredoctor.sivota.mapper;

import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.model.Risposta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RispostaMapper {

    @Mappings({
            @Mapping(target = "uuidRisposta", source = "uuidRisposta"),
            @Mapping(target = "testo", source = "testo"),
            @Mapping(target = "domanda", source = "domanda"),
            @Mapping(target = "isSelected", source = "isSelected"),
            @Mapping(target = "risultatoNumerico", source = "risultatoNumerico"),
            @Mapping(target = "votanti", source = "votanti")
    })
    RispostaDTO rispostaToRispostaDTO(Risposta risposta);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuidRisposta", source = "uuidRisposta"),
            @Mapping(target = "testo", source = "testo"),
            @Mapping(target = "domanda", source = "domanda"),
            @Mapping(target = "isSelected", source = "isSelected"),
            @Mapping(target = "risultatoNumerico", source = "risultatoNumerico"),
            @Mapping(target = "votanti", source = "votanti")
    })
    Risposta rispostaDTOToRisposta(RispostaDTO rispostaDTO);

    List<RispostaDTO> risposteToRispostaDTOs(List<Risposta> risposte);
    List<Risposta> rispostaDTOsToRisposte(List<RispostaDTO> rispostaDTOs);

}
