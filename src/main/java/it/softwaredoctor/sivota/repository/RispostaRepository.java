package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.model.Risposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RispostaRepository extends JpaRepository<Risposta, Long> {


}
