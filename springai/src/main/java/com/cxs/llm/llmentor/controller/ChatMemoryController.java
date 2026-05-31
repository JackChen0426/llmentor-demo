package com.cxs.llm.llmentor.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxingsheng
 * @date 2026/5/27 11:03
 */
@RestController
@RequestMapping("/memory")
public class ChatMemoryController implements InitializingBean {


    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    private ChatClient chatClient;

    @Autowired
    private ChatMemory chatMemory;


    @GetMapping("/call")
    public String call(String message) {
        return null;
    }

    @GetMapping("/call1")
    public String call1(String message) {
        List<Message> messages = new ArrayList<>();

        messages.add(new SystemMessage("你是一个旅行推荐师"));
        messages.add(new UserMessage("我想去新疆玩"));
        messages.add(new AssistantMessage("好的，我知道了，你要去新疆，请问你准备什么时候去"));
        messages.add(new UserMessage("我准备元旦的时候去玩"));
        messages.add(new AssistantMessage("好的，请问你想玩那些内容？"));

        messages.add(new UserMessage("我喜欢自然风光"));

        Prompt prompt = new Prompt(messages);
        return dashScopeChatModel.call(prompt).getResult().getOutput().getText();
    }


    @GetMapping("/callConversation")
    public Flux<String> callConversation(String message, String chatId, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream().content();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(3).build();

        this.chatClient = ChatClient.builder(dashScopeChatModel)
                // 实现 Logger 的 Advisor
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new SimpleLoggerAdvisor())
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                ).build();
    }
}
