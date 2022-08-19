package dcom.focuz.api.domain.notification.dto;

import dcom.focuz.api.domain.notification.Notification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String message;
        private String url;
        private Boolean isRead;
        private LocalDateTime created;

        public static Info of(Notification notification) {
            return Info.builder()
                    .message(notification.getMessage())
                    .url(notification.getUrl())
                    .isRead(notification.getIsRead())
                    .created(notification.getCreated())
                    .build();
        }

        public static List<Info> of(List<Notification> notifications) {
            return notifications.stream().map(Info::of).collect(Collectors.toList());
        }
    }
}
