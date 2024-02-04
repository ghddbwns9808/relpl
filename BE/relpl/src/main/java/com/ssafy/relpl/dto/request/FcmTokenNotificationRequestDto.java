package com.ssafy.relpl.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenNotificationRequestDto {
    private Long targetUserId;
    private String title;
    private String body;
    // private String image;
    // private Map<String, String> data;

    @Builder
    public FcmTokenNotificationRequestDto(Long targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
        // this.image = image;
        // this.data = data;
    }

    public String getTokenUid() {
    }

    public String getTokenStr() {
    }
}
