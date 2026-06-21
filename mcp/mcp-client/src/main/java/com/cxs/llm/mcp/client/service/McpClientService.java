package com.cxs.llm.mcp.client.service;

/**
 * @author chenxingsheng
 * @date 2026/6/21 22:01
 */

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class McpClientService {

    @Autowired
    private List<McpSyncClient> mcpSyncClients;


    public void sendRequestToMcpServers() {
        for (McpSyncClient mcpSyncClient : mcpSyncClients) {
            System.out.println("Sending request to MCP server: " + mcpSyncClient);
        }
    }
}
