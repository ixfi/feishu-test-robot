package com.xxx;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FeishuBotController {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String VERIFICATION_TOKEN = "【你的飞书机器人验证策略验证TOKEN】";
    private static final String APP_ID = "【你的飞书机器人应用凭证AppID】";
    private static final String APP_SECRET = "【你的飞书机器人应用凭证AppSecret】";



    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody String rawBody) {
        try {
            System.out.println("收到原始请求: " + rawBody);
            Map<String, Object> body = objectMapper.readValue(rawBody, Map.class);
            Map<String, Object> header = (Map<String, Object>) body.get("header");
            String eventType = (String) header.get("event_type");
            String token = (String) header.get("token");
            System.out.println("事件类型: " + eventType);
            System.out.println("SECRET: " + token);
            // 检查
            if (!VERIFICATION_TOKEN.equals(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }

            // 处理消息
            if ("im.message.receive_v1".equals(eventType)) {
                new Thread(() -> {
                    try {
                        Map<String, Object> event = (Map<String, Object>) body.get("event");
                        handleMessage(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            return ResponseEntity.ok(Map.of("code", 0, "msg", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }

    //处理消息（去除AI在回复时会@用户）
    private void handleMessage(Map<String, Object> event) throws Exception {
        Map<String, Object> message = (Map<String, Object>) event.get("message");
        String chatId = (String) message.get("chat_id");
        String contentStr = (String) message.get("content");

        //解析用户问题
        Map<String, String> content = objectMapper.readValue(contentStr, Map.class);
        String userQuestion = content.get("text");

        //最重要的去除代码
        userQuestion = userQuestion.replaceAll("@_user_\\d+", "").trim();
        System.out.println("用户问: " + userQuestion);

        //调用AI
        String answer = chatLanguageModel.generate(userQuestion);
        System.out.println("AI 答: " + answer);

        //发送回复
        sendReply(chatId, answer);
    }

    //发送消息到飞书
    private void sendReply(String chatId, String answer) throws Exception {
        // 每次发送都重新获取SECRET（Token），解决过期问题
        String token = getTenantAccessToken();
        String url = "【你的飞书机器人消息请求地址】";//详细参阅：https://open.feishu.cn/document/server-docs/im-v1/message/create?appId=cli_a956c83589789cc9&lang=zh-CN

        Map<String, Object> body = new HashMap<>();
        body.put("receive_id", chatId);
        body.put("content", objectMapper.writeValueAsString(Map.of("text", answer)));
        body.put("msg_type", "text");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("发送结果: " + response.getBody());
    }

    //获取飞书SECRET（Token）
    private String getTenantAccessToken() {
        String url = "【你的飞书机器人获取SECRET（Token）请求地址】";//详细参阅：https://open.feishu.cn/document/server-docs/authentication-management/access-token/tenant_access_token_internal
        Map<String, String> req = new HashMap<>();
        req.put("app_id", APP_ID);
        req.put("app_secret", APP_SECRET);

        ResponseEntity<Map> resp = restTemplate.postForEntity(url, req, Map.class);
        return (String) resp.getBody().get("tenant_access_token");
    }
}