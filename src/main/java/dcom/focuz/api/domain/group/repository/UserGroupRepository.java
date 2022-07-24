package dcom.focuz.api.domain.group.repository;

import dcom.focuz.api.domain.group.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
