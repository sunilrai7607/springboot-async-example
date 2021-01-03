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
