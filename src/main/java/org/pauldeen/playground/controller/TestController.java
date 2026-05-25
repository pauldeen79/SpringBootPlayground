package org.pauldeen.playground.controller;

import org.pauldeen.playground.model.TestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping(value = "/{id}", produces = "application/json")
    public TestResponse get(@PathVariable int id) {
        return TestResponse.builder()
                .id(id)
                .name("name for %d".formatted(id))
                .build();
    }
}
