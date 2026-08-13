package com.gunitha.springAiAndMcp.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    public ChatClient mcpChatClient(ChatClient.Builder chatClientBuilder, AsyncMcpToolCallbackProvider asyncMcpToolCallbackProvider) {
        return chatClientBuilder
                .defaultTools(asyncMcpToolCallbackProvider)
                .build();
    }
}
