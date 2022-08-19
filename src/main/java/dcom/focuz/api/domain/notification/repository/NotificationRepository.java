package dcom.focuz.api.domain.notification.repository;

import dcom.focuz.api.domain.notification.Notification;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.user = :user ORDER BY n.id desc")
    List<Notification> findAllByUser(User user);
}