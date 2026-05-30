package com.cxs.llm.llmentor.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxingsheng
 * @date 2026/5/27 11:03
 */
@RestController
@RequestMapping("/prompt/template")
public class PromptTemplateController implements InitializingBean {


    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    private ChatClient chatClient;


    @Value("classpath:templates/open_source_system_prompt.st")
    private Resource systemPrompt;


    @Override
    public void afterPropertiesSet() throws Exception {
        chatClient = ChatClient.builder(dashScopeChatModel)
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(DashScopeChatOptions.builder().temperature(0.7).build())
                .build();
    }


    @RequestMapping("/call")
    public Flux<String> call(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String template = """
                请给我推荐几个关于{topic}的开源项目
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("topic", message);

        return chatClient.prompt(promptTemplate.create()).system("你是一个专业的的github项目收集人员").stream().content();
    }


    @RequestMapping("/file")
    public Flux<String> file(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        HashMap variables = new HashMap();
        variables.put("language", "Java");
        variables.put("topic", message);
        PromptTemplate promptTemplate = PromptTemplate.builder().resource(systemPrompt).variables(variables).build();

        return chatClient.prompt(promptTemplate.create(Map.of("topic", message))).system("你是一个专业的的github项目收集人员").stream().content();
    }

}
