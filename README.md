## Springboot Async example
@Async Annotation that marks a method as a candidate for <i>asynchronous</i> execution.
Can also be used at the type level, in which case all of the type's methods are
considered as asynchronous. Note, however, that {@code @Async} is not supported
on methods declared within a class


```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setThreadNamePrefix("userThread-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
```
Service class
1. Parse Json file and convert into Java objects
2. Persist into MongoDB
```java
package com.sbr.platform.async.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbr.platform.async.mapper.UserEntityMapper;
import com.sbr.platform.async.model.domain.Address;
import com.sbr.platform.async.model.domain.CustomUser;
import com.sbr.platform.async.model.entity.UserEntity;
import com.sbr.platform.async.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsyncService {

    private final UserEntityMapper userEntityMapper;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public AsyncService(UserEntityMapper userEntityMapper, UserEntityRepository userEntityRepository) {
        this.userEntityMapper = userEntityMapper;
        this.userEntityRepository = userEntityRepository;
    }

    @Async
    public CompletableFuture<List<UserEntity>> populate(){
        long startTime = System.currentTimeMillis();
        List<UserEntity> userEntity = populateAddress(populateUsers());
        log.info("Saving all the users size: {} , thread : {} ",userEntity.size(),Thread.currentThread().getName());
        userEntity = userEntityRepository.saveAll(userEntity);
        long endTime = System.currentTimeMillis();
        log.info("Total Time : {} ", endTime - startTime);
        return CompletableFuture.completedFuture(userEntity);
    }

    @Async
    public CompletableFuture<List<UserEntity>> findAll(){
        List<UserEntity> userEntity = userEntityRepository.findAll();
        log.info("Saving all the users size: {} , thread : {} ",userEntity.size(),Thread.currentThread().getName());
        return CompletableFuture.completedFuture(userEntity);
    }

    public List<UserEntity>  populateUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<CustomUser> customUsersArray = Arrays.asList(objectMapper.readValue(ResourceUtils.getFile(
                    "classpath:data/user-info.json"), CustomUser[].class));
            return customUsersArray.stream().map(userEntityMapper::convert).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public List<UserEntity> populateAddress(List<UserEntity> customUsers) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Address>  addresses = Arrays.asList(objectMapper.readValue(ResourceUtils.getFile(
                    "classpath:data/user-address.json"),Address[].class));
            Map<String, Address> addressMap = addresses.stream().collect(Collectors.toMap(Address::getId, address -> address));
            return customUsers.parallelStream()
                    .map(
                            userEntity -> {
                                userEntity.setAddress(userEntityMapper.convert(addressMap.get(userEntity.getId())));
                                return userEntity;
                            }
                    ).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return customUsers;
    }
}

```
Controller class to create users and read all users.
```java
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

```

