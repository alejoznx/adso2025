package comm.app.backend.repository;

import com.app.backend.model.user;
import org.springframework.data.jpa.Repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<user> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email):
}