package com.gunitha.springAiAndMcp.util;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.annotation.McpSampling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TicketSamplingProvider {

    @Autowired
    private ChatModel chatModel;

    @McpSampling(clients = "mcpRemote")
    public Mono<McpSchema.CreateMessageResult> handleSamplingRequest(McpSchema.CreateMessageRequest createMessageRequest) {
        log.info("Received MCP Sampling request from Server {} and with System prompt {}", "mcpRemote", createMessageRequest.systemPrompt());
        List<Message> messages = new ArrayList<>();
        if (Objects.nonNull(createMessageRequest.systemPrompt())) {
            messages.add(new SystemMessage(createMessageRequest.systemPrompt()));
        }
        String userText = createMessageRequest.messages().stream()
                .filter(samplingMessage -> samplingMessage.content() instanceof McpSchema.TextContent
                        && samplingMessage.role().name().equalsIgnoreCase(McpSchema.Role.USER.name()))
                .map(samplingMessage -> ((McpSchema.TextContent) samplingMessage.content()).text())
                .collect(Collectors.joining("\n"));
        messages.add(new UserMessage(userText));
        ChatResponse chatResponse = chatModel.call(new Prompt(messages));
        if (Objects.isNull(chatResponse.getResult())) {
            throw new IllegalStateException("LLM returned no results for the MCP sampling request");
        }
        String generated = chatResponse.getResult().getOutput().getText();
        String model = chatResponse.getMetadata().getModel();
        log.info("LLM produced sampling response using model {}:{}", model, generated);
        assert generated != null;
        return Mono.just(McpSchema.CreateMessageResult.builder(McpSchema.Role.ASSISTANT, generated, model).build());
    }
}
