package com.cxs.llm.llmentor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author chenxingsheng
 * @date 2026/5/27 11:28
 */
@RestController
@RequestMapping("/stream")
public class StreamController {

    @GetMapping(value = "/sse/flux")
    public Flux<String> fluxStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> "Stream element - " + seq);
    }
}
