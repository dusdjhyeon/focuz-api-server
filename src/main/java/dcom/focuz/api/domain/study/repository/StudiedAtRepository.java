package dcom.focuz.api.domain.study.repository;

import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudiedAtRepository extends JpaRepository<StudiedAt, Integer> {
    @Query("select s from StudiedAt s where s.user = :user and s.studyHour = :studyHour")
    Optional<StudiedAt> findByUserAndStudyHour(User user, LocalDateTime studyHour);
    @Query("select s from StudiedAt s where s.studyHour >= :startDate and s.studyHour < :endDate and s.user = :user")
    List<StudiedAt> findAllByUserAndStudyHour(User user, LocalDateTime startDate, LocalDateTime endDate);
}
