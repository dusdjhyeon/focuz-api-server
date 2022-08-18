package dcom.focuz.api.domain.study.repository;

import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.study.TempStudy;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TempStudyRepository extends CrudRepository<TempStudy, String> {
    Optional<TempStudy> findByUserId(Integer userId);
}
