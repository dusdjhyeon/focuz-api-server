package dcom.focuz.api.domain.study.repository;

import dcom.focuz.api.domain.study.TempStudy;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TempStudyRepository extends CrudRepository<TempStudy, String> {
    Optional<TempStudy> findByUserId(Integer userId);
}
