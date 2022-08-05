package com.fedex.assessment.aggregator.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShipmentAPIGateway {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${shipment.api.base.url}")
	private String shipment_api_url;

	public Map<String, List<String>> getShipmentsInfo(String shipmentsStr) {
		System.out.println("Calling shipment API: "+shipmentsStr);
		ParameterizedTypeReference<HashMap<String, List<String>>> responseType = new ParameterizedTypeReference<HashMap<String, List<String>>>() {
		};
		System.out.println("shipmentsStr :"+shipmentsStr);
		String URL = shipment_api_url + shipmentsStr;

		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		Map<String, List<String>> jsonDictionary = restTemplate.exchange(request, responseType).getBody();
		System.out.println("Response received from shipment API: "+jsonDictionary);
		return jsonDictionary;

	}

}
