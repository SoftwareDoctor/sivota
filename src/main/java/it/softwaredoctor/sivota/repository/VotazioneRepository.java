package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotazioneRepository extends JpaRepository<Votazione, Long> {

    Optional<Votazione> findByUuidVotazione(UUID uuidVotazione);

    void deleteByUserAndUuidVotazione(User user, UUID uuidVotazione);
}
