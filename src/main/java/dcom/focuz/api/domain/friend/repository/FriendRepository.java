package dcom.focuz.api.domain.friend.repository;

import dcom.focuz.api.domain.friend.Friend;
import dcom.focuz.api.domain.friend.FriendState;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Query(value = "select f from Friend f left join fetch f.user where f.state = :state and f.targetUser = :targetUser",
            countQuery = "select count(f) from Friend f where f.state = :state and f.targetUser = :targetUser")
    Page<Friend> findAllByStateAndTargetUser(FriendState state, User targetUser, Pageable pageable);

    Optional<Friend> findByUserAndTargetUser(User user, User targetUser);

    @Query(value = "select f from Friend f left join fetch f.targetUser where f.state = :state and f.user = :user",
            countQuery = "select count(f) from Friend f where f.state = :state and f.user = :user")
    Page<Friend> findAllByStateAndUser(FriendState state, User user, Pageable pageable);
}
