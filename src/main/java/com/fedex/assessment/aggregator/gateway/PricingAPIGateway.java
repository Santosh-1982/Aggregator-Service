package com.fedex.assessment.aggregator.gateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PricingAPIGateway {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${pricing.api.base.url}")
	private String pricing_api_url;
	
	
	
	
	public Map<String, BigDecimal> getPricing(String countriesStr) throws Exception {
		System.out.println("calling pricing API: ");
		ParameterizedTypeReference<HashMap<String, BigDecimal>> responseType = new ParameterizedTypeReference<HashMap<String, BigDecimal>>() {
		};
		System.out.println("countriesStr :" + countriesStr);
		String URL = pricing_api_url + countriesStr;

		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		Map<String, BigDecimal> responseMap = restTemplate.exchange(request, responseType).getBody();
		System.out.println("Response received from pricing API: "+responseMap);
		return responseMap;

	}

}
