package com.cxs.llm.function;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenxingsheng
 * @date 2026/6/4 17:09
 */
@RestController
@RequestMapping("/functionCall")
public class FunctionCallController {

    @GetMapping("/hello")
    public String functionCall() {
        return "hello functionCall";
    }

}
