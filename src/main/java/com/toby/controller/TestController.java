package com.toby.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ResponseEntity test() {

        return new ResponseEntity<>("hello",  HttpStatus.OK);
    }
}
