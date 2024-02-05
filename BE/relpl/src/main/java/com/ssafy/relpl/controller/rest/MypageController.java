package com.ssafy.relpl.controller.rest;

import com.ssafy.relpl.dto.request.MypageChangePasswordRequest;
import com.ssafy.relpl.dto.request.MypageChangeRequest;
import com.ssafy.relpl.service.MypageService;
import com.ssafy.relpl.service.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/user/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;


    @PutMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<CommonResult> changeProfile(@ModelAttribute MypageChangeRequest request) throws IOException {
        log.info("changeProfile");
        return mypageService.changeProfile(request);
    }


    @PutMapping("/password")
    public ResponseEntity<CommonResult> changePassword(@RequestBody MypageChangePasswordRequest request) {
        log.info("changePassword");
        return mypageService.changePassword(request);
    }


}
