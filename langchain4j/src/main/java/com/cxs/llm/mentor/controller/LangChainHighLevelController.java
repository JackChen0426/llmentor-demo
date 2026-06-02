package com.cxs.llm.mentor.controller;

import com.cxs.llm.mentor.bean.Book;
import com.cxs.llm.mentor.service.LangChainAiService;
import com.cxs.llm.mentor.service.LangChainMemoryAiService;
import com.cxs.llm.mentor.tools.TemperatureTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author chenxingsheng
 * @date 2026/6/2 13:39
 */
@RestController
@RequestMapping("/langchain/high")
public class LangChainHighLevelController implements InitializingBean {

    @Autowired
    private LangChainAiService langChainAiService;

    @Autowired
    OpenAiChatModel chatModel;

    @RequestMapping("/hello")
    public String hello() {
        return langChainAiService.chat("日本都有哪些美食？");
    }

    @RequestMapping("/helloStream")
    public Flux<String> helloStream(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Book book = langChainAiService.getBooks();
        return Flux.just(book.toString());
    }

    @RequestMapping("/helloStream2")
    public Flux<String> helloStream2(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return langChainAiService.chatStream2("日本都有哪些美食？");
    }

    @RequestMapping("/structure")
    public Flux<String> structure(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Book book = langChainAiService.getBooks();
        return Flux.just(book.toString());
    }

    private LangChainMemoryAiService langChainMemoryAiService;


    @RequestMapping("/memory")
    public String memory(HttpServletResponse response, String msg, String memoryId) {
        response.setCharacterEncoding("UTF-8");
        return langChainMemoryAiService.chatMemory(memoryId, msg);
    }

    @RequestMapping("/toolUsing")
    public String toolUsing(HttpServletResponse response, String msg) {
        response.setCharacterEncoding("UTF-8");
        LangChainAiService langChainAiService1 = AiServices.builder(LangChainAiService.class)
                .tools(new TemperatureTools())
                .chatModel(chatModel)
                .build();

        return langChainAiService1.chat(msg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        langChainMemoryAiService = AiServices.builder(LangChainMemoryAiService.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
//                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder().id(memoryId).maxMessages(10).chatMemoryStore(redisChatMemoryStore).build())
                .build();
    }
}