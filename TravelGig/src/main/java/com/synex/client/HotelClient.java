package com.synex.client;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
public class HotelClient {
    private static final String hotelMicroserviceSearch = "http://localhost:8383/searchHotel/";
    private static final String hotelMicroserviceFilter = "http://localhost:8383/filterHotel/";
    private static final String hotelMicroserviceDetails = "http://localhost:8383/searchHotelDetails/";
    private static final String bookingDetailsUrl = "http://localhost:8383/booking-details";
    private static final String submitQuestionUrl = "http://localhost:8383/api/questions/submit";
    private static final String answerQuestionUrl = "http://localhost:8383/api/questions/answer/";
    private static final String getQuestionsByUserUrl = "http://localhost:8383/api/questions/user/";
   
    public JsonNode searchHotel(String searchString) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(hotelMicroserviceSearch + searchString, Object.class);
        Object objects = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(objects, JsonNode.class);
    }

    public JsonNode searchHotelDetails(int hotelId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(hotelMicroserviceDetails + hotelId, Object.class);
        Object objects = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(objects, JsonNode.class);
    }

    public Map<String, Object> getBookingDetails(int hotelId, int noGuests, String checkInDate, String checkOutDate, String roomType, int noRooms) {
        // Construct the URL with query parameters
        String url = String.format("%s?hotelId=%d&noGuests=%d&checkInDate=%s&checkOutDate=%s&roomType=%s&noRooms=%d",
                bookingDetailsUrl, hotelId, noGuests, checkInDate, checkOutDate, roomType, noRooms);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        return responseEntity.getBody();
    }

    public JsonNode searchFilterHotel(JsonNode node) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(node.toString(), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(hotelMicroserviceFilter, request, Object.class);
        Object objects = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(objects, JsonNode.class);
    }
    
    public String submitQuestion(Map<String, String> questionDetails) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(questionDetails, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(submitQuestionUrl, request, String.class);
        return response.getBody();
    }
    
    public String answerQuestion(long questionId, Map<String, String> answerDetails) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(answerDetails, headers);

        ResponseEntity<String> response = restTemplate.exchange(answerQuestionUrl + questionId, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getQuestionsByUser(long userId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            getQuestionsByUserUrl + userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }
    
}
