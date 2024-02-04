package com.ssafy.relpl.service;

import com.ssafy.relpl.message.FcmMessage;  // 수정된 import
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class FirebaseCloudMessageDataService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageDataService.class);
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/myfinalpannelrace/messages:send";

    private final ObjectMapper objectMapper;
    private final List<String> clientTokens = new ArrayList<>();

    public FirebaseCloudMessageDataService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeDataMessage(String targetToken, String title, String body) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);

        FcmMessage.Message message = new FcmMessage.Message();
        message.setToken(targetToken);
        message.setData(map);

        FcmMessage fcmMessage = new FcmMessage(false, message);

        return objectMapper.writeValueAsString(fcmMessage);
    }

    public void sendDataMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeDataMessage(targetToken, title, body);
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

    public void addToken(String token) {
        clientTokens.add(token);
    }

    public int broadCastDataMessage(String title, String body) throws IOException {
        for (String token : clientTokens) {
            logger.debug("broadcastmessage : {},{},{}", token, title, body);
            sendDataMessageTo(token, title, body);
        }
        return clientTokens.size();
    }
}
