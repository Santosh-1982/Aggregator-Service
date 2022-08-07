package com.fedex.assessment.aggregator.endpoint.camel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fedex.assessment.aggregator.constants.Constant;
import com.fedex.assessment.aggregator.gateway.TrackingAPIGateway;
import com.fedex.assessment.aggregator.model.TrackingRequest;
import com.fedex.assessment.aggregator.repository.ApiResponseRepo;

@Component
public class TrackingMsgProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(TrackingMsgProcessor.class);

	@Autowired
	TrackingAPIGateway trackingGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		String reqStr = null;
		TrackingRequest trackingRequest = null;
		for (Exchange ex : exx) {
			trackingRequest = ex.getIn().getBody(TrackingRequest.class);
			for (String str : trackingRequest.getRequestStr()) {
				log.info(trackingRequest.getUuid() + "  " + Constant.TRACKING_REQ + "  " + str);

				apiResponseRepo.createRequest(trackingRequest.getUuid(), Constant.TRACKING_REQ, str);
				if (null != reqStr) {
					reqStr = reqStr + "," + str;
				} else {
					reqStr = str;
				}

			}
		}
		log.info("Messages read from queue :" + reqStr);
		Map<String, String> resMap = null;
		try {
			resMap = trackingGateway.getTrackInfo(reqStr);
		} catch (Exception e) {
			log.info("Error occured while calling tracking api");
			resMap = new HashMap<String, String>();
			for (String str : trackingRequest.getRequestStr()) {
				{
					log.info("puuting error :" + str);
					resMap.put(str, Constant.APR_ERROR);
				}
			}
		}
		apiResponseRepo.saveTrackingResponse(Constant.TRACKING_REQ, resMap);

	}

}
