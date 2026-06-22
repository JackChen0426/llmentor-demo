package com.cxs.llm.mcp.client.service;

/**
 * @author chenxingsheng
 * @date 2026/6/21 22:01
 */

import com.alibaba.fastjson2.JSON;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Service
@Slf4j
public class McpClientService {


    @Autowired
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    private ChatClient chatClient;

    @Autowired
    private OpenAiChatModel chatModel;

    @Autowired
    private List<McpSyncClient> mcpSyncClients;


    /**
     * 直接调用mcp server
     */
    public McpSchema.CallToolResult callTool(String type) {
        String toolName = "getWeather";
        Map<String, Object> param = new HashMap<>();
        param.put("city", "北京");

        for (McpSyncClient client : mcpSyncClients) {
            McpSchema.Implementation clientInfo = client.getClientInfo();
            McpSchema.Implementation serverInfo = client.getServerInfo();
            log.info("clientInfo: {}", JSON.toJSONString(clientInfo));
            log.info("serverInfo: {}", JSON.toJSONString(serverInfo));
            try {
                if (clientInfo.title().contains(type)) {
                    log.info("开始调用mcp服务");
                    McpSchema.CallToolRequest request = McpSchema.CallToolRequest.builder().name(toolName).arguments(param).build();
                    McpSchema.CallToolResult result = client.callTool(request);
                    log.info("callTool result: {}", result);
                    return result;
                }
            } catch (Exception ex) {
                log.error("调用mcp服务出错", ex);
            }
            log.info("====================================================");
        }
        return null;
    }


    @PostConstruct
    public void init() {
        ToolCallback[] toolCallbacks = toolCallbackProvider.getToolCallbacks();

        this.chatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbacks)
                .build();

//        this.chatClient = ChatClient.builder(chatModel)
//                .defaultTools(new WeatherService())
//                .build();
    }

    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
