package com.fedex.assessment.aggregator.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fedex.assessment.aggregator.repository.entity.ApiResponse;

@Service
public class ApiResponseRepo {
	private static final Logger log = LoggerFactory.getLogger(ApiResponseRepo.class);
	@Autowired
	SpringDataApiReposeRepository repo;
	
	@Transactional
	public void createRequest(UUID uuid,String reqType, String requestParam) {
		System.out.println("inserting values : uuid: "+uuid+" reqType: "+reqType+" requestParam: "+requestParam);
		repo.createRequest(uuid,reqType,requestParam,Timestamp.valueOf(LocalDateTime.now()));
		log.info("response saved to DB for "+reqType);
	}

	@Transactional
	public void savePricingResponse(String resType, Map<String, BigDecimal> responseMap) {
		responseMap.forEach((key, value) -> repo.saveRespose(value.toString(), resType, key.trim()));
		log.info("Pricing response saved to DB for ");
	}

	@Transactional
	public void saveTrackingResponse(String resType, Map<String, String> responseMap) {
		responseMap.forEach((key, value) -> repo.saveRespose(value, resType, key.trim()));
		log.info("Tracking response saved to DB for ");
	}

	@Transactional
	public void saveShipmentResponse(String resType, Map<String, List<String>> responseMap) {
		// log.info("Response saving");
		responseMap.forEach((key, value) -> repo.saveRespose(
				value.stream().map(Object::toString).collect(Collectors.joining(",")), resType, key.trim()));
		log.info("Shipment response saved to DB for ");
	}

	public ApiResponse getResponse(UUID uuid, String reqParam) {
		// log.info("resType :"+resType);
		// log.info("reqParam :"+reqParam);
		List<ApiResponse> res = repo.findRespose(uuid, reqParam.trim());
		if (null != res && res.size() > 0) {
			return res.get(0);
		}
		// log.info("res.get(0) : null");
		return null;
	}

	@Transactional
	public void deleteApiResponse() {
		repo.deleteApiRespose();
		log.info("Earlier response has been deleted from database table ");

	}

}
