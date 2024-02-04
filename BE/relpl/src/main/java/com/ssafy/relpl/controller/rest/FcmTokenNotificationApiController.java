package com.ssafy.relpl.controller.rest;

import com.ssafy.relpl.dto.request.FcmTokenNotificationRequestDto;
import com.ssafy.relpl.service.FcmTokenNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "FCM Notification 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
// fcm 서비스를 호출하여 푸시 알림을 보내는 로직 정의하는 클래스
public class FcmTokenNotificationApiController {
    private final FcmTokenNotificationService fcmNotificationService;

    @Operation(summary = "알림 보내기")
    @PostMapping()
    public String sendNotificationByToken(@RequestBody FcmTokenNotificationRequestDto requestDto) {
        return fcmNotificationService.sendNotificationByToken(requestDto);
    }

}
