package com.cxs.llm.llmentor.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenxingsheng
 * @date 2026/5/27 11:03
 */
@RestController
@RequestMapping("/client")
public class ChatClientController implements InitializingBean {


    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    private ChatClient chatClient;


    @RequestMapping("/simpleCall")
    public String simpleCall(String message) {
        return chatClient.prompt(message).call().content();
    }

    @RequestMapping("/call")
    public String call(String message) {
        return chatClient.prompt(message).system("用繁体中文回答问题").call().content();
    }

    @RequestMapping("/callUser")
    public String callUser(String message) {
        return chatClient.prompt(message).user(message).call().content();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                ).defaultSystem("请用英文回答问题")
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .temperature(0.7)
                                .build()
                )
                .build();
    }
}
