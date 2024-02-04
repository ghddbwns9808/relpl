package com.ssafy.relpl.service;

import com.ssafy.relpl.dto.request.FcmTokenNotificationRequestDto;

import java.util.List;

public interface TokenMapper {
    List<FcmTokenNotificationRequestDto> selectAll();  // 토큰 정보 조회 메서드
    void insert(FcmTokenNotificationRequestDto token);  // 토큰 정보 추가 메서드
    void update(FcmTokenNotificationRequestDto token);  // 토큰 정보 갱신 메서드
}