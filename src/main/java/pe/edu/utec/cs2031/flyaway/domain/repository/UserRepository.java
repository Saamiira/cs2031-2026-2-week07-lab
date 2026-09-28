package pe.edu.utec.cs2031.flyaway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.cs2031.flyaway.domain.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
