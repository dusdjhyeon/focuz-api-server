package dcom.focuz.api.domain.notification.service;

import dcom.focuz.api.domain.notification.dto.NotificationResponseDto;
import dcom.focuz.api.domain.notification.repository.NotificationRepository;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<NotificationResponseDto.Info> getMyNotification() {
        User currentUser = userService.getCurrentUser();

        return NotificationResponseDto.Info.of(notificationRepository.findAllByUser(currentUser));
    }
}

