package com.fedex.assessment.endpoint.rest;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fedex.assessment.aggregator.endpoint.rest.AggController;
import com.fedex.assessment.aggregator.service.AggService;
import com.fedex.assessment.config.RequestCounter;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AggControllerTest {

	private MockMvc mockMvc;
	@InjectMocks
	private AggController aggController;
	@Mock
	private AggService aggService;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(aggController).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCounter() {

		//aggController.aggregationGet("CN,NL", "123456891,109347263", "123456891,109347263");
		RequestCounter counter = new RequestCounter();
		for (int i = 0; i < 20; i++) {
			counter.increment();
			aggController.aggregationGet("CN,NL", "123456891,109347263", "123456891,109347263");
		}
		assertEquals(500, counter.getCount());
	}

	@Test
	public void testCounterWithConcurrency() throws InterruptedException {
		int numberOfThreads = 10;
		ExecutorService service = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		RequestCounter counter = new RequestCounter();
		for (int i = 0; i < numberOfThreads; i++) {
			service.execute(() -> {
				counter.increment();
				latch.countDown();
			});
		}
		latch.await();
		assertEquals(numberOfThreads, counter.getCount());
	}

}
