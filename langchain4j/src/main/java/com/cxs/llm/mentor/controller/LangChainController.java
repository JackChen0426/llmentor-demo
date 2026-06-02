package com.cxs.llm.mentor.controller;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;

/**
 * @author chenxingsheng
 * @date 2026/6/2 13:39
 */
@RestController
@RequestMapping("/langchain")
public class LangChainController {

    @Autowired
    OpenAiChatModel chatModel;

    @Autowired
    OpenAiStreamingChatModel streamingChatModel;

    @RequestMapping("/hello")
    public String hello() {
        return chatModel.chat("你好,你是谁？");
    }

    @RequestMapping("/streamTest")
    public Flux<String> streamTest(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        Flux<String> flux = Flux.create(fluxSink -> {
            streamingChatModel.chat("你好,你是谁？", new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    fluxSink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    fluxSink.complete();
                }

                @Override
                public void onError(Throwable error) {
                    fluxSink.error(error);
                }
            });
        });


        return flux;
    }

    @RequestMapping("/streamHello")
    public Flux<String> streamHello(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Flux<String> flux = Flux.create(fluxSink -> {
            streamingChatModel.chat("你好,给我推荐一些新疆美食？", new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    fluxSink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    fluxSink.complete();
                }

                @Override
                public void onError(Throwable error) {
                    fluxSink.error(error);
                }
            });
        });
        return flux;
    }


    @RequestMapping("/memory")
    public String memory(HttpServletResponse response) {
        List<ChatMessage> messages = new ArrayList<>();

        //第一轮对话
        messages.add(systemMessage("你是一个点餐助手"));
        messages.add(userMessage("给我点一个汉堡，两个鸡腿，一杯可乐"));
        AiMessage answer = chatModel.chat(messages).aiMessage();
        System.out.println(answer);
        System.out.println("======");

        messages.add(answer);

        //第二轮对话
        messages.add(userMessage("刚才菜点多了，去掉一个鸡腿，再加一杯可乐吧?"));
        AiMessage answer1 = chatModel.chat(messages).aiMessage();
        System.out.println(answer1);
        System.out.println("======");

        messages.add(answer1);

        //第三轮对话
        messages.add(userMessage("我现在总共点了哪些东西？"));
        AiMessage answer2 = chatModel.chat(messages).aiMessage();
        System.out.println(answer2);
        System.out.println("======");

        return answer2.text();
    }

}