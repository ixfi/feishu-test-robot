package com.xxx;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LangChainConfig {

    // 从yml读取配置
    @Value("${langchain4j.open-ai.chat.base-url}")
    private String baseUrl;
    @Value("${langchain4j.open-ai.chat.api-key}")
    private String apiKey;
    @Value("${langchain4j.open-ai.chat.model-name}")
    private String modelName;
    @Value("${langchain4j.open-ai.chat.temperature}")
    private Double temperature;
    @Value("${langchain4j.open-ai.chat.max-tokens}")
    private Integer maxTokens;
    @Value("${langchain4j.open-ai.chat.timeout}")
    private Duration timeout;
    @Value("${langchain4j.open-ai.chat.max-retries}")
    private Integer maxRetries;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(timeout)
                .maxRetries(maxRetries)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}