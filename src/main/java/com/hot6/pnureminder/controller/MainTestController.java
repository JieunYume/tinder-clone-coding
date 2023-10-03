package com.hot6.pnureminder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainTestController {

    @GetMapping("/main")
    public ResponseEntity<String> tester(){
        return ResponseEntity.ok("main page test OK");
    }

}
