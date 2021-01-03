package com.sbr.platform.async;

import com.sbr.platform.async.repository.UserEntityRepository;
import com.sbr.platform.async.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        //userEntityRepository.saveAll(asyncService.populate());
        userEntityRepository.deleteAll();
    }
}
