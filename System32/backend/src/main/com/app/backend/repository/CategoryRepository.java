package comm.app.backend.repository;

import com.app.backend.model.user;
import org.springframework.data.jpa.Repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<category> findByName(String name);

    boolean existsByName(String name);

    
}