package com.fedex.assessment.aggregator.endpoint.camel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fedex.assessment.aggregator.constants.Constant;
import com.fedex.assessment.aggregator.gateway.PricingAPIGateway;
import com.fedex.assessment.aggregator.repository.ResponseRepo;

@Component
public class PricingMsgProcessor implements Processor {

	@Autowired
	PricingAPIGateway pricingGateway;
	@Autowired
	private ResponseRepo responseRepoImp;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Exchange> ex = (List<Exchange>) exchange.getIn().getBody();
		String reqStr = null;
		for (Exchange exx : ex) {
			if (null != reqStr) {
				reqStr = reqStr + "," + exx.getIn().getBody(String.class);
			} else {
				reqStr = exx.getIn().getBody(String.class);
			}
		}
		System.out.println("Messages read from queue :" + reqStr);
		Map<String, BigDecimal> resMap = null;
		try {
			resMap = pricingGateway.getPricing(reqStr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occured while pricing shipment api");
			resMap = new HashMap<String, BigDecimal>();
			List<String> restedStr = Arrays.asList(reqStr.split(","));
			for (String str : restedStr) {
				{
					System.out.println("puuting error :" + reqStr);
					resMap.put(str, new BigDecimal("-99.99"));
				}
			}
		}
		responseRepoImp.savePricingResponse(Constant.PRICING_REQ, resMap);

	}

}
