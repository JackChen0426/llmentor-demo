package com.cxs.llm.mcp.server.stdio;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenxingsheng
 * @date 2026/6/21 17:24
 */
@Configuration
public class WeatherConfiguration {

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        // 自动扫描 WeatherService 中带有 @Tool 注解的方法
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
