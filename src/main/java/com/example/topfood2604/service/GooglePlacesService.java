package com.example.topfood2604.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class GooglePlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> findRestaurantPhoto(String restaurantName) throws Exception {

        String url = "https://places.googleapis.com/v1/places:searchText";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask",
                "places.id,places.displayName,places.formattedAddress,places.location,places.photos");

        String body = """
                {
                  "textQuery": "%s 台北 餐廳",
                  "languageCode": "zh-TW",
                  "regionCode": "TW"
                }
                """.formatted(restaurantName);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode firstPlace = root.path("places").get(0);

        if (firstPlace == null) {
            return Map.of("error", "找不到餐廳");
        }

        String name = firstPlace.path("displayName").path("text").asText();
        String address = firstPlace.path("formattedAddress").asText();

        double lat = firstPlace.path("location").path("latitude").asDouble();
        double lng = firstPlace.path("location").path("longitude").asDouble();

        String mapUrl = "https://www.google.com/maps?q=" + lat + "," + lng;

        String embedMapUrl =
                "https://maps.google.com/maps?q="
                        + lat + ","
                        + lng
                        + "&z=16&output=embed";

        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        result.put("lat", String.valueOf(lat));
        result.put("lng", String.valueOf(lng));
        result.put("mapUrl", mapUrl);
        result.put("embedMapUrl", embedMapUrl);

        JsonNode firstPhoto = firstPlace.path("photos").get(0);

        if (firstPhoto == null) {
            result.put("error", "這家餐廳沒有照片");
            return result;
        }

        String photoName = firstPhoto.path("name").asText();

        String photoUrl =
                "https://places.googleapis.com/v1/"
                        + photoName
                        + "/media?maxWidthPx=800&key="
                        + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        result.put("photoUrl", photoUrl);

        return result;
    }
}