package code.vasilyevps.library.repository;

import code.vasilyevps.library.entity.reader.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    boolean existsByEmail(String email);
}
