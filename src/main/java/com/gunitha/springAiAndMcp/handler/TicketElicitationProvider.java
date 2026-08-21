package com.gunitha.springAiAndMcp.handler;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpElicitation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class TicketElicitationProvider {

    @McpElicitation(clients = "mcpRemote")
    public Mono<McpSchema.ElicitResult> handleMcpElicitRequest(McpSchema.ElicitRequest elicitRequest) {
        log.info("Received Elicitation Request from sever with details {}", elicitRequest.message());
        Map<String, Object> userResponse = Map.of("priority", "HIGH", "phone", "123-145-1478");
        log.info("Responding to elicitation with Accept and info {}", userResponse);
        return Mono.just(McpSchema.ElicitResult.builder(McpSchema.ElicitResult.Action.ACCEPT)
                .content(userResponse)
                .build());
    }
}
