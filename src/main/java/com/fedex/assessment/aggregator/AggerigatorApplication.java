package com.fedex.assessment.aggregator;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class AggerigatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggerigatorApplication.class, args);
	}

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(200);
		executor.setMaxPoolSize(200);
		executor.setQueueCapacity(600);
		executor.setThreadNamePrefix("concurrentThread-");
		executor.initialize();
		executor.setKeepAliveSeconds(10);
		return executor;
	}

}
