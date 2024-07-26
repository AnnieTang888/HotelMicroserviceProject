package com.synex.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class InsuranceClient {
    private static final String INSURANCE_MICROSERVICE_BASE = "http://localhost:8383/api/";
    private static final String SAVE_INSURANCE_URL = INSURANCE_MICROSERVICE_BASE + "insurance";
    private static final String GET_CITIES_BY_PROVINCE_URL = INSURANCE_MICROSERVICE_BASE + "cities?provinceId=";
    private static final String GET_ALL_PROVINCES_URL = INSURANCE_MICROSERVICE_BASE + "provinces";

    @Autowired
    private RestTemplate restTemplate;

    public JsonNode saveInsurance(JsonNode insuranceData) {
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(SAVE_INSURANCE_URL, insuranceData, JsonNode.class);
        return response.getBody();
    }

    public JsonNode getCitiesByProvince(int provinceId) {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(GET_CITIES_BY_PROVINCE_URL + provinceId, JsonNode.class);
        return response.getBody();
    }

    public JsonNode getAllProvinces() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(GET_ALL_PROVINCES_URL, JsonNode.class);
        return response.getBody();
    }
}


/*@Component
public class InsuranceClient {

    private static final String insuranceMicroserviceBase = "http://localhost:8383/api/";
    private static final String saveInsuranceUrl = insuranceMicroserviceBase + "insurance";
    private static final String getCitiesByProvinceUrl = insuranceMicroserviceBase + "cities?provinceId=";
    private static final String getAllProvincesUrl = insuranceMicroserviceBase + "provinces";

    //@Autowired
    //private RestTemplate restTemplate;

    public JsonNode saveInsurance(JsonNode insuranceData) {
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(saveInsuranceUrl, insuranceData, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(responseEntity.getBody(), JsonNode.class);
    }

    public JsonNode getCitiesByProvince(int provinceId) {
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(getCitiesByProvinceUrl + provinceId, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(responseEntity.getBody(), JsonNode.class);
    }

    public JsonNode getAllProvinces() {
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(getAllProvincesUrl, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(responseEntity.getBody(), JsonNode.class);
    }
}*/
