package dcom.focuz.api.domain.user.repository;

import dcom.focuz.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u left join fetch u.groups ug left join fetch ug.group g where u.id = :id")
    Optional<User> findUserAllById(Integer id);
    Optional<User> findUserById(Integer id);
    Optional<User> findUserByEmail(String id);
}
