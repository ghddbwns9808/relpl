package com.ssafy.relpl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.relpl.dto.request.FcmTokenNotificationRequestDto;
import com.ssafy.relpl.message.FcmMessage;  // 수정된 import
import jakarta.annotation.PostConstruct;
//import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class FirebaseCloudMessageService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageService.class);
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/myfinalpannelrace/messages:send";

    private final TokenMapper repo;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HashMap<String, String> clientUidTokenMap = new HashMap<>();

    public FirebaseCloudMessageService(TokenMapper repo) {
        this.repo = repo;
    }

    @PostConstruct
    private void initTokens() {
        List<FcmTokenNotificationRequestDto> tokenList = repo.selectAll();
        for (FcmTokenNotificationRequestDto token : tokenList) {
            clientUidTokenMap.put(token.getTokenUid(), token.getTokenStr());
        }
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeDataMessage(String targetToken, String title, String body, String genre, String type, String userId) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);
        map.put("genre", genre);
        map.put("type", type);
        map.put("userId", userId);

        FcmMessage.Message message = new FcmMessage.Message();
        message.setToken(targetToken);
        message.setData(map);

        FcmMessage fcmMessage = new FcmMessage(false, message);
        return objectMapper.writeValueAsString(fcmMessage);
    }

    public void sendMessageTo(String targetToken, String title, String body, String genre, String type, String userId) throws IOException {
        String message = makeDataMessage(targetToken, title, body, genre, type, userId);
        logger.info("message : {}", message);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        logger.info("FCM response: {}", response.body().string());
    }

    public void addToken(String token, String userUID) {
        if (clientUidTokenMap.containsKey(userUID)) {
            repo.update(new TokenDTO("-1", token, userUID));
        } else {
            logger.info("addToken ->{} : {}", token, userUID);
            repo.insert(new TokenDTO("-1", token, userUID));
        }
        clientUidTokenMap.put(userUID, token);
    }

    public int broadCastMessage(String title, String body, String genre, String type, String userId) throws IOException {
        for (String token : clientUidTokenMap.values()) {
            logger.debug("broadcastmessage : {},{},{},{},{},{}", token, title, body, userId);
            sendMessageTo(token, title, body, genre, type, userId);
        }
        return clientUidTokenMap.size();
    }
}
