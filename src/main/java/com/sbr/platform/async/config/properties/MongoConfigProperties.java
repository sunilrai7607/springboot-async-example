package com.sbr.platform.async.config.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;


@Slf4j
@Data
@ConfigurationProperties(prefix = "sbr.service.mongodb.config")
public class MongoConfigProperties {

    private MongoProperties primary = new MongoProperties();
    private String retentionTime;

    @PostConstruct
    public void printProperties() {
        log.info("MongoConfigProperties : primary : {} secondary: {}", primary.getAuthenticationDatabase());
    }

}
