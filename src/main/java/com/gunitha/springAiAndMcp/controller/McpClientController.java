package com.gunitha.springAiAndMcp.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class McpClientController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatClient mcpChatClient;

    @GetMapping("chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt(message)
                .advisors(new SimpleLoggerAdvisor())
                .user(message).call().content();
    }

    @GetMapping("mcpChat")
    public Mono<String> mcpChat(@RequestParam String message) {
        // Mono.defer ensures that the entire chat setup execution moves
        // to a background thread before AsyncMcpToolCallbackProvider runs
        return Mono.defer(() ->
                mcpChatClient.prompt(message)
                        .advisors(new SimpleLoggerAdvisor())
                        .user(message)
                        .stream()
                        .content()
                        .collect(Collectors.joining())
        ).subscribeOn(Schedulers.boundedElastic()); // Safe offloading context switch
    }



}
