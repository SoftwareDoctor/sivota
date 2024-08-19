package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    boolean existsByUsernameOrEmail(String email, String username);
}
