package com.fedex.assessment.aggregator;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fedex.assessment.aggregator.service.HouseKeepingService;

@SpringBootApplication
@EnableScheduling
public class AggerigatorApplication {
	@Autowired
	HouseKeepingService houseKeepingService;

	public static void main(String[] args) {
		SpringApplication.run(AggerigatorApplication.class, args);
	}
	
	@Scheduled(initialDelay = 600000, fixedDelay = 600000)
	public void scheduleHousekeeping()
	{
		houseKeepingService.deleteApiResponse();
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
