package com.synex.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class BookingClient {
 private static final String bookingMicroserviceUrl = "http://localhost:8484/booking";

 // Method to forward booking data to BookingMicroService
 public JsonNode sendBooking(JsonNode bookingNode) {
     RestTemplate restTemplate = new RestTemplate();
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);

     HttpEntity<String> request = new HttpEntity<>(bookingNode.toString(), headers);
     ResponseEntity<Object> responseEntity = restTemplate.postForEntity(bookingMicroserviceUrl, request, Object.class);
     Object objects = responseEntity.getBody();
     ObjectMapper mapper = new ObjectMapper();
     return mapper.convertValue(objects, JsonNode.class);
  }
 
 public JsonNode getBookingsByStatus(String status) {
     RestTemplate restTemplate = new RestTemplate();
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);
     String url = "http://localhost:8484/bookings" + "/status/" + status; 
     System.out.println("Requesting URL: " + url);

     try {
         ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
         return response.getBody();
     } catch (Exception e) {
         System.out.println("Error fetching bookings by status: " + e.getMessage());
         throw new RuntimeException("Error fetching bookings by status", e);
     }
 }

 public JsonNode updateBookingStatus(Long bookingId, String newStatus) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Construct the requestBody JSON with the new status
	    ObjectNode requestBody = JsonNodeFactory.instance.objectNode();
	    requestBody.put("status", newStatus);
	    HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

	    // Correctly constructing the URL to point to the cancel endpoint
	    String url = "http://localhost:8484/bookings/" + bookingId + "/status";

	    // Using the exchange method to send the POST request
	    ResponseEntity<JsonNode> response = restTemplate.exchange(
	        url, 
	        HttpMethod.POST, 
	        request, 
	        JsonNode.class
	    );
	    
	    return response.getBody();
	}

 public ResponseEntity<String> cancelBooking(int bookingId) {
     RestTemplate restTemplate = new RestTemplate();
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);

     HttpEntity<String> request = new HttpEntity<>(headers);
     String url = "http://localhost:8484" + "/cancel/" + bookingId;

     ResponseEntity<String> response = restTemplate.exchange(
         url, 
         HttpMethod.POST, 
         request, 
         String.class
     );
     
     return response;
 }
 
 public JsonNode submitReview(JsonNode reviewData) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    String url = "http://localhost:8484/api/reviews/create";

	    HttpEntity<String> request = new HttpEntity<>(reviewData.toString(), headers);
	    ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
	    return response.getBody();
	}
 
 //Method to get the average rating for a hotel
 public Double getAverageRatingForHotel(int hotelId) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String url = "http://localhost:8484/api/reviews/average-rating/" + hotelId;

    try {
        ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
        return response.getBody();
    } catch (Exception e) {
        System.out.println("Error fetching average rating: " + e.getMessage());
        return 0.0; // Default to 0 if there's an error
   }
}

  // Method to get reviews by hotel ID
  public JsonNode getReviewsByHotelId(int hotelId) {
     RestTemplate restTemplate = new RestTemplate();
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);

     String url = "http://localhost:8484/api/reviews/by-hotels/" + hotelId;

     try {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        return response.getBody();
     } catch (Exception e) {
        System.out.println("Error fetching reviews: " + e.getMessage());
        throw new RuntimeException("Error fetching reviews", e);
     }
  }


}
