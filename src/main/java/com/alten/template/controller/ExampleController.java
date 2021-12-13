package com.alten.template.controller;

import com.alten.template.model.ExamplePOJO;
import com.alten.template.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @Autowired
    private ExampleService testService;

    @GetMapping("/test")
    public ResponseEntity<ExamplePOJO> hitSecuredEndPoint(@AuthenticationPrincipal Jwt principal) {
        return new ResponseEntity<>(testService.getTestObject(principal.getSubject()), HttpStatus.OK);
    }

    @GetMapping("/test/exception")
    public ResponseEntity<ExamplePOJO> hitSecuredEndPointThatShowsExceptionHandling() {
        return new ResponseEntity<>(testService.getBadTestObjectToShowErrorHandling(), HttpStatus.OK);
    }

    @GetMapping("/public/test")
    public ResponseEntity<ExamplePOJO> hitPublicEndpoint() {
        return new ResponseEntity<>(testService.getTestObject(), HttpStatus.OK);
    }
}