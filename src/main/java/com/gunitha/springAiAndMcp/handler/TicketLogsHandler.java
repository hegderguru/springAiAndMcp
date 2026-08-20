package com.gunitha.springAiAndMcp.handler;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpLogging;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TicketLogsHandler {

    // 💡 Keep the destructured handler clean and accurately named
    @McpLogging(clients = "mcpRemote")
    public void onServerLog(McpSchema.LoggingLevel level, String logger, String data) {
        log.info("[Destructured Client] Log from source {} [{}] -> {}", logger, level.name(), data);
    }

   /* @McpLogging(clients = "mcpRemote")
    public void onServerLog2(McpSchema.LoggingMessageNotification notification) {
        log.info("[Envelope Client] Log from source {} [{}] -> {}", notification.logger(), notification.level().name(), notification.data());
    }*/
}
