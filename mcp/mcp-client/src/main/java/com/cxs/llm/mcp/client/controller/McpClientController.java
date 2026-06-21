package com.cxs.llm.mcp.client.controller;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MCP Client 控制器
 * 演示通过三种传输方式（STDIO / SSE / Streamable HTTP）调用 MCP Server
 *
 * @author chenxingsheng
 * @date 2026/6/21 22:03
 */
@RestController
@RequestMapping("/mcp")
public class McpClientController {

    @Autowired
    @Qualifier("weatherStdio")
    private McpSyncClient stdioClient;

    @Autowired
    @Qualifier("weatherSse")
    private McpSyncClient sseClient;

    @Autowired
    @Qualifier("weatherStreamable")
    private McpSyncClient streamableClient;

    // ==================== STDIO ====================

    /**
     * 列出 STDIO 模式 MCP Server 的所有工具
     */
    @GetMapping("/stdio/tools")
    public Object listStdioTools() {
        return stdioClient.listTools();
    }

    /**
     * 通过 STDIO 模式调用天气查询工具
     */
    @GetMapping("/stdio/weather")
    public Object callStdioWeather(@RequestParam(defaultValue = "北京") String city) {
        CallToolResult result = stdioClient.callTool(
                new CallToolRequest("getWeather", Map.of("city", city))
        );
        return result.content();
    }

    // ==================== SSE ====================

    /**
     * 列出 SSE 模式 MCP Server 的所有工具
     */
    @GetMapping("/sse/tools")
    public Object listSseTools() {
        return sseClient.listTools();
    }

    /**
     * 通过 SSE 模式调用天气查询工具
     */
    @GetMapping("/sse/weather")
    public Object callSseWeather(@RequestParam(defaultValue = "北京") String city) {
        CallToolResult result = sseClient.callTool(
                new CallToolRequest("getWeather", Map.of("city", city))
        );
        return result.content();
    }

    // ==================== Streamable HTTP ====================

    /**
     * 列出 Streamable HTTP 模式 MCP Server 的所有工具
     */
    @GetMapping("/streamable/tools")
    public Object listStreamableTools() {
        return streamableClient.listTools();
    }

    /**
     * 通过 Streamable HTTP 模式调用天气查询工具
     */
    @GetMapping("/streamable/weather")
    public Object callStreamableWeather(@RequestParam(defaultValue = "北京") String city) {
        CallToolResult result = streamableClient.callTool(
                new CallToolRequest("getWeather", Map.of("city", city))
        );
        return result.content();
    }

    // ==================== 统一调用 ====================

    /**
     * 同时列出三种传输模式 MCP Server 的所有工具
     */
    @GetMapping("/all/tools")
    public Map<String, Object> listAllTools() {
        return Map.of(
                "stdio", stdioClient.listTools(),
                "sse", sseClient.listTools(),
                "streamable", streamableClient.listTools()
        );
    }

    /**
     * 同时通过三种传输模式调用天气查询工具
     */
    @GetMapping("/all/weather")
    public Map<String, Object> callAllWeather(@RequestParam(defaultValue = "北京") String city) {
        return Map.of(
                "stdio", stdioClient.callTool(new CallToolRequest("getWeather", Map.of("city", city))).content(),
                "sse", sseClient.callTool(new CallToolRequest("getWeather", Map.of("city", city))).content(),
                "streamable", streamableClient.callTool(new CallToolRequest("getWeather", Map.of("city", city))).content()
        );
    }
}
