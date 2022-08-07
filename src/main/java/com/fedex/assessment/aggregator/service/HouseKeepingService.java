package com.fedex.assessment.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fedex.assessment.aggregator.repository.ApiResponseRepo;

@Service
public class HouseKeepingService {
	private static final Logger log = LoggerFactory.getLogger(HouseKeepingService.class);

	@Autowired
	private ApiResponseRepo apiResponseRepo;

	public void deleteApiResponse() {

		apiResponseRepo.deleteApiResponse();
		log.info("deleted response data");
	}

}
