package com.cho.library.backend.cmmn;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping("/say")
    public Map<String, String> sayHello() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Hello from Spring Boot!");
        return result;
    }
}
