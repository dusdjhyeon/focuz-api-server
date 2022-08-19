package dcom.focuz.api.domain.group.repository;

import dcom.focuz.api.domain.group.Group;
import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.UserGroupPermission;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    @Query("select ug from UserGroup ug left join fetch ug.user where ug.group = :groupObj and ug.permission = :permission")
    List<UserGroup> findAllByGroupAndPermission(Group groupObj, UserGroupPermission permission);

    @Query("select ug from UserGroup ug left join fetch ug.user where ug.group = :group and ug.permission in (dcom.focuz.api.domain.group.UserGroupPermission.OWNER, dcom.focuz.api.domain.group.UserGroupPermission.MANAGER)")
    List<UserGroup> findAllByGroupHasManagePermission(Group group);

    @Query("select ug from UserGroup ug left join fetch ug.user where ug.group = :group and ug.permission in (dcom.focuz.api.domain.group.UserGroupPermission.OWNER, dcom.focuz.api.domain.group.UserGroupPermission.MANAGER, dcom.focuz.api.domain.group.UserGroupPermission.MEMBER)")
    List<UserGroup> findAllMemberByGroup(Group group);

    Optional<UserGroup> findByUserAndGroup(User user, Group groupObj);
    List<UserGroup> findAllByUser(User user);
}
