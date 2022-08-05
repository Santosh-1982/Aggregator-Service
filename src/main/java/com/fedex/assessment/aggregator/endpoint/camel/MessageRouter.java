package com.fedex.assessment.aggregator.endpoint.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageRouter extends RouteBuilder {

	@Value("${pricing-requests.queue}")
	private String pricing_requests_queue_name;

	@Value("${tracking-requests.queue}")
	private String tracking_requests_queue_name;

	@Value("${shipment-requests.queue}")
	private String shipments_requests_queue_name;
	
	@Value("${request.batch.size}")
	private String requests_batch_size;
	
	@Value("${request.batch.copletion.time}")
	private String requests_batch_completion_time;

	@Autowired
	PricingMsgProcessor pricingProcessor;
	
	@Autowired
	TrackingMsgProcessor trackingProcessor;
	
	@Autowired
	ShipmentMsgProcessor shipmentProcessor;

	
	@Override
	public void configure() throws Exception {
		from("jms:" + pricing_requests_queue_name).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(pricingProcessor);

		from("jms:" + tracking_requests_queue_name).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(trackingProcessor);

		from("jms:" + shipments_requests_queue_name).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(shipmentProcessor);

	}

}
