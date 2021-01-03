package com.sbr.platform.async.controller;

import com.sbr.platform.async.model.entity.UserEntity;
import com.sbr.platform.async.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "api/v1/async")
public class AsyncController {

    private final AsyncService asyncService;

    @Autowired
    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @PostMapping("/users")
    public ResponseEntity<CompletableFuture<List<UserEntity>>> findAllUser() {
        asyncService.populate();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    public CompletableFuture<ResponseEntity> createUsers() {
        return asyncService.findAll().thenApply(ResponseEntity::ok);
    }
}
