package dcom.focuz.api.domain.group.repository;

import dcom.focuz.api.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("select g from Group g left join fetch g.users ug left join fetch ug.user where g.id = :id")
    Optional<Group> findById(Integer id);
}