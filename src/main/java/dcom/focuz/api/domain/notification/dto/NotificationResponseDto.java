package dcom.focuz.api.domain.notification.dto;

import lombok.*;

import java.time.LocalDateTime;

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
    }
}
