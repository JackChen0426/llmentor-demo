package com.cxs.llm.llmentor.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author chenxingsheng
 * @date 2026/5/27 11:03
 */
@RestController
public class ChatModelController {


    @Autowired
    private DashScopeChatModel dashScopeChatModel;


    @RequestMapping("/call/string")
    public String callString(String message) {
        return dashScopeChatModel.call(message);
    }

    @RequestMapping(value = "/call/stringV2", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> callStringV2(String message) {
        return dashScopeChatModel.stream(message);
    }


    @RequestMapping("/call/messages")
    public String callMessages(String message) {
        Message systemMessage = new SystemMessage("你是一个翻译者，请把用户的内容翻译成英文");
        Message userMessage = new UserMessage(message);
        return dashScopeChatModel.call(systemMessage, userMessage);
    }

    @RequestMapping("/call/prompt")
    public ChatResponse callPrompt(String message) {
        Message systemMessage = new SystemMessage("你是一个翻译者，请把用户的内容翻译成英文");
        Message userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return dashScopeChatModel.call(prompt);
    }

    @RequestMapping("/call/promptV2")
    public String callPromptV2(String message) {
        SystemMessage systemMessage = new SystemMessage("请如实回答问题");
        Message userMsg = new UserMessage(message);

        ChatOptions chatOptions = ChatOptions.builder().model("deepseek-v3").build();
        Prompt prompt = new Prompt.Builder().messages(systemMessage, userMsg).chatOptions(chatOptions).build();

        return dashScopeChatModel.call(prompt).getResult().getOutput().getText();
    }
    @RequestMapping("/stream/string")
    public Flux<String> callStreamString(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return dashScopeChatModel.stream(message);
    }




}
