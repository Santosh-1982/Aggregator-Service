package com.fedex.assessment.aggregator.endpoint.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fedex.assessment.aggregator.model.PricingRequest;
import com.fedex.assessment.aggregator.model.Response;
import com.fedex.assessment.aggregator.model.ShipmentRequest;
import com.fedex.assessment.aggregator.model.TrackingRequest;
import com.fedex.assessment.aggregator.service.AggService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-01T22:33:25.097Z[GMT]")
@RestController
@Slf4j
public class AggController {

	private static final Logger log = LoggerFactory.getLogger(AggController.class);

	@Autowired
	AggService aggService;
	@Autowired
	ProducerTemplate producerTemplate;

	@Value("${pricing-requests.queue}")
	private String pricing_requests_queue_name;

	@Value("${tracking-requests.queue}")
	private String tracking_requests_queue_name;

	@Value("${shipment-requests.queue}")
	private String shipments_requests_queue_name;

	@RequestMapping(value = "/v1/aggregation-api", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Response> aggregationGet(
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = false, schema = @Schema()) @Valid @RequestParam(value = "pricing", required = true) String pricing,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = false, schema = @Schema()) @Valid @RequestParam(value = "track", required = true) String track,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = false, schema = @Schema()) @Valid @RequestParam(value = "shipments", required = true) String shipments) {

		try {
			Response response = new Response();

			PricingRequest pricingRequest = new PricingRequest(UUID.randomUUID(), generateSet(pricing));
			TrackingRequest trackingRequest = new TrackingRequest(UUID.randomUUID(), generateSet(track));
			ShipmentRequest shipmentRequest = new ShipmentRequest(UUID.randomUUID(), generateSet(shipments));
			producerTemplate.sendBody("jms:" + pricing_requests_queue_name, pricingRequest);
			producerTemplate.sendBody("jms:" + tracking_requests_queue_name, trackingRequest);
			producerTemplate.sendBody("jms:" + shipments_requests_queue_name, shipmentRequest);
			log.info("message has been put in queue");
			response = aggService.buidAggRes(pricingRequest, trackingRequest, shipmentRequest);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<Response>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Set<String> generateSet(String reqStr) {
		Set<String> set = new HashSet<String>();
		String[] strArray = reqStr.split(",");
		for (String str : strArray) {
			set.add(str);
		}
		return set;
	}

}
