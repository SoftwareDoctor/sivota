package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuidUser(UUID uuidUser);

//    List <Votazione> findAllByUuidUser(UUID uuidUser);

    List <VotazioneDTO> findAllByUuidUser(UUID uuidUser);

//    Optional <User> findByUsername(String username);

    boolean existsByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String email, String username);


}
