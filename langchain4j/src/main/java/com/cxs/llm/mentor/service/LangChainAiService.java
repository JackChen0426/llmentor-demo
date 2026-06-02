package com.cxs.llm.mentor.service;

import com.cxs.llm.mentor.bean.Book;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * @author chenxingsheng
 * @date 2026/6/2 15:40
 */
@AiService
public interface LangChainAiService {


    String chat(String message);

    Flux<String> chatStream(String message);

    @SystemMessage("你是一个毒舌博主，擅长怼人")
    @UserMessage("针对用户的内容：{{topic}}，先复述一遍他的问题，然后再回答")
    Flux<String> chatStream2(String topic);

    @SystemMessage("你是一个资深的Java开发专家")
    @UserMessage("帮我推荐一本Java的数据")
    Book getBooks();


}
