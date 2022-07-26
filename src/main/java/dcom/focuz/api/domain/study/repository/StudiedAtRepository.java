package dcom.focuz.api.domain.study.repository;

import dcom.focuz.api.domain.study.StudiedAt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudiedAtRepository extends JpaRepository<StudiedAt, Integer> {
}
