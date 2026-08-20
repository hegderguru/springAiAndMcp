package com.gunitha.springAiAndMcp.util;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.AsyncMcpToolCallback;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class McpToolUtil {

    public static ToolCallback[] selectedToolsFor(List<McpSyncClient> mcpSyncClients, String serverName, String toolName) {
        return mcpSyncClients.stream()
                .flatMap(mcpSyncClient -> mcpSyncClient.listTools()
                        .tools().stream().filter(tool -> matches(mcpSyncClient.getServerInfo().name(), serverName)
                                && matches(tool.name(), toolName))
                        .map(tool -> (ToolCallback) SyncMcpToolCallback.builder()
                                .mcpClient(mcpSyncClient)
                                .tool(tool)
                                .build()))
                .toArray(ToolCallback[]::new);
    }

    private static boolean matches(String name, String hint) {
        return Objects.isNull(hint) || hint.isBlank()
                || name.toLowerCase().contains(hint.toLowerCase());
    }

    /*public static Mono<ToolCallback[]> selectedTools(List<McpAsyncClient> mcpAsyncClients, String serverName, String toolName) {
        return Flux.fromIterable(mcpAsyncClients)
                .flatMap(mcpAsyncClient -> mcpAsyncClient.listTools()
                        .flatMapIterable(McpSchema.ListToolsResult::tools)
                        .filter(tool -> matches(mcpAsyncClient.getServerInfo().name(), serverName) && matches(tool.name(), toolName))
                        .flatMap(tool -> mcpAsyncClient.setLoggingLevel(McpSchema.LoggingLevel.INFO)
                                .then(Mono.fromSupplier(() -> (ToolCallback) AsyncMcpToolCallback.builder()
                                        .mcpClient(mcpAsyncClient)
                                        .tool(tool)
                                        .build())))
                )
                .collectList()
                .map(asyncMcpToolCallbacks -> asyncMcpToolCallbacks.toArray(new ToolCallback[0]));
    }*/

    public static Mono<ToolCallback[]> selectedTools(List<McpAsyncClient> mcpAsyncClients, String serverName, String toolName) {
        return Flux.fromIterable(mcpAsyncClients)
                .flatMap(mcpAsyncClient -> mcpAsyncClient.listTools()
                        .flatMapIterable(McpSchema.ListToolsResult::tools)
                        .filter(tool -> matches(mcpAsyncClient.getServerInfo().name(), serverName) && matches(tool.name(), toolName))
                        .map(tool -> (ToolCallback) AsyncMcpToolCallback.builder()
                                .mcpClient(mcpAsyncClient)
                                .tool(tool)
                                .build())
                )
                .collectList()
                .map(asyncMcpToolCallbacks -> asyncMcpToolCallbacks.toArray(new ToolCallback[0]));
    }


}
