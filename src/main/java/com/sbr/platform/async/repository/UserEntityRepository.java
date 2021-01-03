package com.sbr.platform.async.repository;

import com.sbr.platform.async.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity, String> {
}
