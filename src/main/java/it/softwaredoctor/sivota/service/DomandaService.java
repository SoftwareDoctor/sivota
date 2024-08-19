package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.mapper.DomandaMapper;
import it.softwaredoctor.sivota.model.Domanda;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.repository.DomandaRepository;
import it.softwaredoctor.sivota.repository.RispostaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DomandaService {

    private final DomandaRepository domandaRepository;
    private final RispostaService rispostaService;
    private final DomandaMapper domandaMapper;

    @Transactional
    public Domanda createDomanda(DomandaDTO domandaDTO) {
        Domanda domanda = domandaMapper.domandaDTOToDomanda(domandaDTO);
        List<Risposta> risposte = new ArrayList<>();
        for (RispostaDTO rispostaDTO : domandaDTO.getRisposte()) {
            Risposta risposta = rispostaService.createRisposta(rispostaDTO);
            risposta.setDomanda(domanda);
            risposte.add(risposta);
        }
        domanda.setRisposte(risposte);
        domandaRepository.save(domanda);
        return domanda;
    }
}
