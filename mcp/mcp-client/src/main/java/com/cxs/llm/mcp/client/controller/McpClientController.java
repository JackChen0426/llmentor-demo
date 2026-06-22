package com.cxs.llm.mcp.client.controller;

import com.cxs.llm.mcp.client.service.McpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    McpClientService mcpClientService;

    @GetMapping("/hello")
    public Object listStdioTools() {
        return "hello mcp client";
    }


    @GetMapping("/chat")
    public String chat(@RequestParam("msg") String msg) {

        return mcpClientService.chat(msg);
    }


    @GetMapping("/callTool")
    public Object callTool(@RequestParam("type") String type) {
        return mcpClientService.callTool(type);
    }

}
