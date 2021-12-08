package com.agungdh.bpkad.sipdtosimdamigrator.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloWorldResource {
    @GetMapping("")
    public ResponseEntity<String>helloWorld() {
        return ResponseEntity.ok().body("Hello, World!");
    }
}
