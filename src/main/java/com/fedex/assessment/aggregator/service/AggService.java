package com.fedex.assessment.aggregator.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fedex.assessment.aggregator.constants.Constant;
import com.fedex.assessment.aggregator.model.PricingRequest;
import com.fedex.assessment.aggregator.model.Response;
import com.fedex.assessment.aggregator.model.ShipmentRequest;
import com.fedex.assessment.aggregator.model.TrackingRequest;
import com.fedex.assessment.aggregator.repository.ApiResponseRepo;
import com.fedex.assessment.aggregator.repository.entity.ApiResponse;

@Service
@Async
public class AggService {
	private static final Logger log = LoggerFactory.getLogger(AggService.class);
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	public Response buidAggRes(PricingRequest pricing, TrackingRequest track, ShipmentRequest shipments)
			throws InterruptedException, ExecutionException {
		Response res = new Response();
		CompletableFuture<Map<String, BigDecimal>> pricingResult = buildPricingRes(pricing);
		CompletableFuture<Map<String, String>> trackingResult = buildTrackingRes(track);
		CompletableFuture<Map<String, List<String>>> shipmentResult = buildShipmentRes(shipments);
		CompletableFuture.allOf(trackingResult, shipmentResult, pricingResult).join();
		res.setPricing(pricingResult.get());
		res.setTrack(trackingResult.get());
		res.setShipments(shipmentResult.get());

		return res;
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, BigDecimal>> buildPricingRes(PricingRequest pricingRequest)
			throws InterruptedException {
		log.info("start buldnig pircing response for :" + pricingRequest);
		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		Set<String> counrtySet = pricingRequest.getRequestStr();

		ApiResponse responseData = null;
		while (counrtySet.size() != resMap.size()) {
			for (String country : counrtySet) {
				if (!resMap.containsKey(country)) {
					responseData = apiResponseRepo.getResponse(pricingRequest.getUuid(), country);
				}
				if (null != responseData) {
					resMap.put(responseData.getReqParam(),
							new BigDecimal(responseData.getResParam()).compareTo(new BigDecimal(Constant.PRICING_API_ERROR)) == 0 ? null
									: new BigDecimal(responseData.getResParam()));
				}
			}
		}
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, String>> buildTrackingRes(TrackingRequest trackingRequest)
			throws InterruptedException {
		log.info("start buldnig tracking response for :" + trackingRequest);
		Map<String, String> resMap = new HashMap<String, String>();
		Set<String> shipmentSet = trackingRequest.getRequestStr();

		ApiResponse responseData = null;
		while (shipmentSet.size() != resMap.size()) {

			for (String shipment : shipmentSet) {
				if (!resMap.containsKey(shipment)) {
					responseData = apiResponseRepo.getResponse(trackingRequest.getUuid(), shipment);
				}
				if (null != responseData) {
					if (responseData.getResParam().equals(Constant.APR_ERROR))
						resMap.put(responseData.getReqParam(), null);
					else
						resMap.put(responseData.getReqParam(), responseData.getResParam());
				}
			}

		}
		log.info("tracking response build complete");
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, List<String>>> buildShipmentRes(ShipmentRequest shipmentRequest)
			throws InterruptedException {
		log.info("start buldnig shipment response for :" + shipmentRequest);
		Map<String, List<String>> resMap = new HashMap<String, List<String>>();
		Set<String> shipmentSet = shipmentRequest.getRequestStr();

		ApiResponse responseData = null;
		while (shipmentSet.size() != resMap.size()) {
			for (String shipment : shipmentSet) {
				if (!resMap.containsKey(shipment)) {
					responseData = apiResponseRepo.getResponse(shipmentRequest.getUuid(), shipment);
				}
				if (null != responseData) {
					if (responseData.getResParam().equals(Constant.APR_ERROR))
						resMap.put(responseData.getReqParam(), null);
					else
						resMap.put(responseData.getReqParam(), Arrays.asList(responseData.getResParam().split(",")));
				}
			}
		}
		return CompletableFuture.completedFuture(resMap);
	}

}
