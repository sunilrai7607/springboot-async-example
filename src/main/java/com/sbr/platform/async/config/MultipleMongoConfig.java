package com.sbr.platform.async.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.sbr.platform.async.config.properties.MongoConfigProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Data
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MongoConfigProperties.class)
@Slf4j
public class MultipleMongoConfig {

    private final MongoConfigProperties mongoConfigProperties;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(mongoFactory(this.mongoConfigProperties.getPrimary()), this.mongoConfigProperties.getPrimary().getDatabase());
    }

    private MongoClient mongoFactory(final MongoProperties mongo) {
        StringBuffer sb = new StringBuffer();
        sb.append("mongodb://");
        sb.append(mongo.getUsername());
        sb.append(":");
        sb.append(mongo.getPassword());
        sb.append("@");
        sb.append(mongo.getHost());
        sb.append(":");
        sb.append(mongo.getPort());
        sb.append("/");
        sb.append(mongo.getDatabase());
        sb.append("?authSource=");
        sb.append(mongo.getAuthenticationDatabase());
        log.info("Connection String : {} ", sb.toString());
        MongoCredential credential = MongoCredential.createCredential(mongo.getUsername(), mongo.getDatabase(), mongo.getPassword());
        log.info("mongoFactory : {} : credential: {} ", mongo, credential);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(sb.toString()))
                .build();
        return MongoClients.create(mongoClientSettings);

    }

}
