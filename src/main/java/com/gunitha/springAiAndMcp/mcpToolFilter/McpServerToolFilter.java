package com.gunitha.springAiAndMcp.mcpToolFilter;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.McpConnectionInfo;
import org.springframework.ai.mcp.McpToolFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class McpServerToolFilter implements McpToolFilter {
    @Override
    public boolean test(McpConnectionInfo mcpConnectionInfo, McpSchema.Tool tool) {
        String serverName = mcpConnectionInfo.initializeResult().serverInfo().name();
        String toolName = tool.name();
        log.info("Servername: {} and tool name:{}",serverName,toolName);

        if("github".equalsIgnoreCase(serverName) || serverName.contains("github")){
            log.info("Ignore tools with server{} and tool {}",serverName,tool);
            return false;
        }
        if(toolName.contains("write")){
            log.info("Ignore tools with server{} and tool {}",serverName,tool);
            return false;
        }
        return true;
    }
}
