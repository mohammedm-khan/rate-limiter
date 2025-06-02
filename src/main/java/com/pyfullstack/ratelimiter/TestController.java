package com.pyfullstack.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TestController {

    @GetMapping("/process1")
    public void process1() {
        log.info(">>>>>>>>> process1");
    }

    @GetMapping("/process2")
    public void process2() {
        log.info(">>>>>>>>> process2");
    }

    @GetMapping("/process3")
    public void process3() {
        log.info(">>>>>>>>> process3");
    }
}
