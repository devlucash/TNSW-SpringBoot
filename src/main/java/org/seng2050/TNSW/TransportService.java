package org.seng2050.TNSW;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransportService {

    @Value("${transport.api.endpoint}")
    private String apiEndpoint;

    @Value("${transport.api.key}")
    private String apiKey;

    public List<StopEvent> getStopEvents(String stop, String departureTime) {
        RestTemplate restTemplate = new RestTemplate();

        String apiCall = "departure_mon";
        Date when;
        try {
            when = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(departureTime);
        } catch (Exception e) {
            throw new RuntimeException("Invalid departure time format", e);
        }

        String url = apiEndpoint + apiCall + "?" + buildQueryParams(stop, when);

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "apikey " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> jsonResponse = response.getBody();

            // Parse the JSON response
            List<Map<String, Object>> stopEventsList = (List<Map<String, Object>>) jsonResponse.get("stopEvents");

            return stopEventsList.stream().map(this::convertToStopEvent).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            System.err.println("API Request failed: " + e.getMessage());
            System.err.println("Response body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch stop events from API", e);
        }
    }

    private String buildQueryParams(String stop, Date when) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

        Map<String, String> params = Map.of(
                "outputFormat", "rapidJSON",
                "coordOutputFormat", "EPSG:4326",
                "mode", "direct",
                "type_dm", "stop",
                "name_dm", stop,
                "depArrMacro", "dep",
                "itdDate", dateFormat.format(when),
                "itdTime", timeFormat.format(when),
                "TfNSWDM", "true"
        );

        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private StopEvent convertToStopEvent(Map<String, Object> event) {
        StopEvent stopEvent = new StopEvent();

        Map<String, Object> transportation = (Map<String, Object>) event.get("transportation");
        stopEvent.setRouteNumber((String) transportation.get("number"));
        stopEvent.setDestination((String) ((Map<String, Object>) transportation.get("destination")).get("name"));

        stopEvent.setLocation((String) ((Map<String, Object>) event.get("location")).get("name"));
        stopEvent.setDepartureTimePlanned((String) event.get("departureTimePlanned"));

        return stopEvent;
    }
}
