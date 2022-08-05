package com.fedex.assessment.aggregator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fedex.assessment.aggregator.constants.Constant;
import com.fedex.assessment.aggregator.model.Response;
import com.fedex.assessment.aggregator.repository.ResponseRepo;
import com.fedex.assessment.aggregator.repository.entity.ResponseData;

@Service
public class AggService {
	@Autowired
	private ResponseRepo responseRepoImp;

	public Response buidAggRes(String pricing, String track, String shipments)
			throws InterruptedException, ExecutionException {
		Response res = new Response();
		responseRepoImp.deleteAll();
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
	private CompletableFuture<Map<String, BigDecimal>> buildPricingRes(String reqStr) throws InterruptedException {
		System.out.println("start buldnig pircing response for :" + reqStr);
		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		List<String> counrtyList = new ArrayList<>(Arrays.asList(reqStr.split(",")));

		ResponseData responseData = null;
		while (counrtyList.size() > 0) {
			for (String country : counrtyList) {
				responseData = responseRepoImp.getResponse(Constant.PRICING_REQ, country);
				if (null != responseData) {
					break;
				}
			}

			if (null != responseData) {
				resMap.put(responseData.getReqParam(),
						new BigDecimal(responseData.getResParam()).compareTo(new BigDecimal("-99.99")) == 0 ? null
								: new BigDecimal(responseData.getResParam()));
				counrtyList.remove(responseData.getReqParam());
			}
		}
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, String>> buildTrackingRes(String reqStr) throws InterruptedException {
		System.out.println("start buldnig tracking response for :" + reqStr);
		Map<String, String> resMap = new HashMap<String, String>();
		List<String> counrtyList = new ArrayList<>(Arrays.asList(reqStr.split(",")));

		ResponseData responseData = null;
		while (counrtyList.size() > 0) {
			for (String country : counrtyList) {
				responseData = responseRepoImp.getResponse(Constant.TRACKING_REQ, country);
				if (null != responseData) {
					break;
				}
			}

			if (null != responseData) {
				if (responseData.getResParam().equals("ERROR"))
					resMap.put(responseData.getReqParam(), null);
				else
					resMap.put(responseData.getReqParam(), responseData.getResParam());
				counrtyList.remove(responseData.getReqParam());
			}
		}
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, List<String>>> buildShipmentRes(String reqStr) throws InterruptedException {
		System.out.println("start buldnig shipment response for :" + reqStr);
		Map<String, List<String>> resMap = new HashMap<String, List<String>>();
		List<String> counrtyList = new ArrayList<>(Arrays.asList(reqStr.split(",")));

		ResponseData responseData = null;
		while (counrtyList.size() > 0) {
			for (String country : counrtyList) {
				responseData = responseRepoImp.getResponse(Constant.SHIPMENT_REQ, country);
				if (null != responseData) {
					break;
				}
			}

			if (null != responseData) {
				if (responseData.getResParam().equals("ERROR"))
					resMap.put(responseData.getReqParam(), null);
				else
					resMap.put(responseData.getReqParam(), Arrays.asList(responseData.getResParam().split(",")));
				counrtyList.remove(responseData.getReqParam());
			}
		}
		return CompletableFuture.completedFuture(resMap);
	}

}
