package com.gunitha.springAiAndMcp.controller;

import com.gunitha.springAiAndMcp.util.McpToolUtil;
import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class McpClientController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatClient mcpChatClient;

    @Autowired
    private List<McpAsyncClient> mcpAsyncClients;

    @GetMapping("chat")
    public String chat(@RequestHeader(required = false) String username, @RequestParam String message) {
        return chatClient.prompt(message + " My username is " + username)
                .advisors(new SimpleLoggerAdvisor())
                .user(message).call().content();
    }

    @GetMapping("mcpChat")
    public Mono<String> mcpChat(@RequestHeader(required = false) String username, @RequestParam String message) {
        // Mono.defer ensures that the entire chat setup execution moves
        // to a background thread before AsyncMcpToolCallbackProvider runs
        Mono<ToolCallback[]> toolCallbacks = McpToolUtil.selectedTools(mcpAsyncClients, "ticket-mcp-server", null); //"create"
       /* return Mono.defer(() ->
                mcpChatClient.prompt(message+" user is "+username)
                        .advisors(new SimpleLoggerAdvisor())
                        //.tools(toolCallbacks)
                        .user(message)
                        .stream()
                        .content()
                        .collect(Collectors.joining())
        ).subscribeOn(Schedulers.boundedElastic()); // Safe offloading context switch*/
        return toolCallbacks.flatMap(toolCallbacks1 -> mcpChatClient.prompt(message + " user is " + username)
                .advisors(new SimpleLoggerAdvisor())
                .tools(toolCallbacks1)
                .toolContext(Map.of("progressToken", UUID.randomUUID().toString()))
                .user(message)
                .stream()
                .content()
                .collect(Collectors.joining())).subscribeOn(Schedulers.boundedElastic()); // Safe offloading context switch
    }

    @GetMapping("mcp-sampling")
    public Mono<String> mcpSampling(@RequestHeader(required = false) String username) {
        Mono<ToolCallback[]> toolCallbacksMono = McpToolUtil.selectedTools(mcpAsyncClients, "ticket-mcp-server", null); //"create"
        return toolCallbacksMono.flatMap(toolCallbacks -> mcpChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("Return the tool response without any changes")
                .tools(toolCallbacks)
                .user("Summarise all of my tickets for username " + username)
                .stream()
                .content()
                .collect(Collectors.joining())).subscribeOn(Schedulers.boundedElastic()); // Safe offloading context switch
    }


}
