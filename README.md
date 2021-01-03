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