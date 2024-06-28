package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.mapper.RispostaMapper;
import it.softwaredoctor.sivota.model.Domanda;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.repository.DomandaRepository;
import it.softwaredoctor.sivota.repository.RispostaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

//    public List<RispostaDTO> findRisposteByUuidVotazione(UUID uuidVotazione) {
//        List <Risposta> risposte= rispostaRepository.findAllByUuidVotazione(uuidVotazione);
//        return rispostaMapper.risposteToRispostaDTOs(risposte);
//    }

//    public void updateRisposta(RispostaDTO rispostaDTO, UUID uuidDomanda) {
//        Risposta risposta = rispostaRepository.findByUuidDomanda(uuidDomanda)
//                .orElseThrow(() -> new EntityNotFoundException("Risposta with UUID " + uuidDomanda + " not found"));
//        risposta.setRisultatoNumerico(rispostaDTO.getRisultatoNumerico());
//        risposta.setIsSelected(rispostaDTO.getIsSelected());
//        rispostaRepository.save(risposta);
//    }



}