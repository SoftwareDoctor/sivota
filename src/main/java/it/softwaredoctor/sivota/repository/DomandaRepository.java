package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.model.Domanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomandaRepository extends JpaRepository<Domanda, Long> {
}
