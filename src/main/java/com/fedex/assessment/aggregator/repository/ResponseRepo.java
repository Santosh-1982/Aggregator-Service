package com.fedex.assessment.aggregator.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fedex.assessment.aggregator.repository.entity.ResponseData;

@Service
public class ResponseRepo {
	@Autowired
	SpringDataReposeRepository repo;

	@Transactional
	public void savePricingResponse(String resType, Map<String, BigDecimal> responseMap) {
		responseMap.forEach((key, value) -> repo.saveAndFlush(
				new ResponseData(resType, key.trim(), Timestamp.valueOf(LocalDateTime.now()), value.toString())));
		System.out.println("Response saved to DB");
	}

	@Transactional
	public void saveTrackingResponse(String resType, Map<String, String> responseMap) {
		responseMap.forEach((key, value) -> repo
				.saveAndFlush(new ResponseData(resType, key.trim(), Timestamp.valueOf(LocalDateTime.now()), value)));
		System.out.println("Response saved to DB");
	}

	@Transactional
	public void saveShipmentResponse(String resType, Map<String, List<String>> responseMap) {
		System.out.println("Response saving");
		responseMap.forEach(
				(key, value) -> repo.saveAndFlush(new ResponseData(resType, key.trim(), Timestamp.valueOf(LocalDateTime.now()),
						value.stream().map(Object::toString).collect(Collectors.joining(",")))));
		System.out.println("Response saved to DB");
	}

	// @Transactional
	public ResponseData getResponse(String resType, String reqParam) {
		System.out.println("resType :"+resType);
		System.out.println("reqParam :"+reqParam);
		List<ResponseData> res = repo.findRespose(resType, reqParam.trim());
		if (null != res && res.size() > 0) {
			System.out.println("Not null res.get(0) :" + res.get(0));
			return res.get(0);
		}
		System.out.println("res.get(0) : null");
		return null;
	}

	@Transactional
	public void deleteAll() {
		repo.deleteAll();

	}

}
