package dcom.focuz.api.domain.notification.controller;

import dcom.focuz.api.domain.notification.dto.NotificationResponseDto;
import dcom.focuz.api.domain.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Notification Controller"})
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    @ApiOperation("현재 온 알림을 가져 옵니다.")
    @GetMapping(value = "/my")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<NotificationResponseDto.Info>> getMyNotification() {
        return null;
    }
}
