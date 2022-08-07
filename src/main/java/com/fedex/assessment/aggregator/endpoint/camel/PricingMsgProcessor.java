package com.fedex.assessment.aggregator.endpoint.camel;

import java.math.BigDecimal;
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
import com.fedex.assessment.aggregator.gateway.PricingAPIGateway;
import com.fedex.assessment.aggregator.model.PricingRequest;
import com.fedex.assessment.aggregator.repository.ApiResponseRepo;

@Component
public class PricingMsgProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(PricingMsgProcessor.class);

	@Autowired
	PricingAPIGateway pricingGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		log.info("message received in processor "+exx.size());
		String reqStr = null;
		PricingRequest pricingRequest = null;
		for (Exchange ex : exx) {
			pricingRequest = ex.getIn().getBody(PricingRequest.class);
			for (String str : pricingRequest.getRequestStr()) {
				log.info(pricingRequest.getUuid() + "  " + Constant.PRICING_REQ + "  " + str);

				apiResponseRepo.createRequest(pricingRequest.getUuid(), Constant.PRICING_REQ, str);
				if (null != reqStr) {
					reqStr = reqStr + "," + str;
				} else {
					reqStr = str;
				}

			}
		}
		log.info("Messages read from queue :" + reqStr);

		Map<String, BigDecimal> resMap = null;
		try {
			resMap = pricingGateway.getPricing(reqStr);
		} catch (Exception e) {
			log.info("Error occured while pricing shipment api");
			resMap = new HashMap<String, BigDecimal>();
			for (String str : pricingRequest.getRequestStr()) {
				{
					log.info("puuting error :" + reqStr);
					resMap.put(str, new BigDecimal(Constant.PRICING_API_ERROR));
				}
			}
		}
		apiResponseRepo.savePricingResponse(Constant.PRICING_REQ, resMap);

	}

}
