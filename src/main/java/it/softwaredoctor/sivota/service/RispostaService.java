package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.mapper.RispostaMapper;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.repository.RispostaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RispostaService {

    private final RispostaRepository rispostaRepository;
    private final RispostaMapper rispostaMapper;

    @Transactional
    public Risposta createRisposta(RispostaDTO rispostaDTO) {
        Risposta risposta = rispostaMapper.rispostaDTOToRisposta(rispostaDTO);
        return rispostaRepository.save(risposta);
    }
}