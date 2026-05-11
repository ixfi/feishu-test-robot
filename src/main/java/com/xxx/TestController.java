package com.xxx;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @GetMapping("/test")//测试接口http://localhost:8080/test
    public String test() {
        System.out.println("开始调用 NewAPI + Ollama...");

        // 浏览器测试
        String answer = chatLanguageModel.generate("你是谁？");

        return "AI 回答：" + answer;
    }
}