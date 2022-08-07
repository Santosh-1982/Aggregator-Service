package com.fedex.assessment.aggregator.endpoint.camel;

import java.util.Arrays;
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
import com.fedex.assessment.aggregator.gateway.ShipmentAPIGateway;
import com.fedex.assessment.aggregator.model.ShipmentRequest;
import com.fedex.assessment.aggregator.repository.ApiResponseRepo;

@Component
public class ShipmentMsgProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(ShipmentMsgProcessor.class);

	@Autowired
	ShipmentAPIGateway shipmentGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		String reqStr = null;
		ShipmentRequest shipmentRequest = null;

		for (Exchange ex : exx) {
			shipmentRequest = ex.getIn().getBody(ShipmentRequest.class);
			for (String str : shipmentRequest.getRequestStr()) {
				log.info(shipmentRequest.getUuid() + "  " + Constant.SHIPMENT_REQ + "  " + str);
				apiResponseRepo.createRequest(shipmentRequest.getUuid(), Constant.SHIPMENT_REQ, str);
				if (null != reqStr) {
					reqStr = reqStr + "," + str;
				} else {
					reqStr = str;
				}
			}
		}
		log.info("Messages read from queue :" + reqStr);
		Map<String, List<String>> resMap = null;
		try {
			resMap = shipmentGateway.getShipmentsInfo(reqStr);
		} catch (Exception e) {
			log.info("Error occured while calling shipment api");
			resMap = new HashMap<String, List<String>>();
			for (String str : shipmentRequest.getRequestStr()) {
				{
					log.info("puuting error :" + reqStr);
					resMap.put(str, Arrays.asList(Constant.APR_ERROR));
				}
			}
		}
		apiResponseRepo.saveShipmentResponse(Constant.SHIPMENT_REQ, resMap);
	}

}
