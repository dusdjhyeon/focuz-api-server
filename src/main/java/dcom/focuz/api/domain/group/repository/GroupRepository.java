package dcom.focuz.api.domain.group.repository;

import dcom.focuz.api.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByNameContains(String groupName);
}