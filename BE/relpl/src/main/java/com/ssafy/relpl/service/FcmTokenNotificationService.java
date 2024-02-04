package com.ssafy.relpl.service;

import com.ssafy.relpl.db.postgre.repository.UserRepository;
import com.ssafy.relpl.dto.request.FcmTokenNotificationRequestDto;
import com.ssafy.relpl.message.FcmMessage;  // 수정된 import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmTokenNotificationService {

    private final FcmMessage firebaseMessaging;  // 수정된 선언
    private final UserRepository usersRepository;

    public String sendNotificationByToken(FcmTokenNotificationRequestDto requestDto) {
        // 타겟 유저 정보 조회
        Optional<Users> user = usersRepository.findById(requestDto.getTargetUserId());

        if (user.isPresent()) {
            // 유저의 Firebase 토큰 확인
            if (user.get().getFirebaseToken() != null) {
                // 알림 객체 생성
                FcmMessage.Notification notification = new FcmMessage.Notification(
                        requestDto.getTitle(),
                        requestDto.getBody(),
                        null  // 이미지는 사용하지 않는 경우, null 또는 원하는 값을 넣어주세요
                );

                // FCM 메시지 객체 생성
                FcmMessage.Message message = new FcmMessage.Message(
                        notification,
                        user.get().getFirebaseToken()
                );

                try {
                    // FCM 메시지 전송
                    firebaseMessaging.send(message);
                    return "알림을 성공적으로 전송했습니다. targetUserId=" + requestDto.getTargetUserId();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 보내기를 실패하였습니다. targetUserId=" + requestDto.getTargetUserId();
                }
            } else {
                return "서버에 저장된 해당 유저의 Firebase Token이 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
            }
        } else {
            return "해당 유저가 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
        }
    }
}
