package com.robynem.mit.web.google;

import com.robynem.mit.web.google.model.PlaceDetails;
import com.robynem.mit.web.google.model.PlaceDetailsExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rrennaloiacono on 17/05/2016.
 */
@Service
public class GoogleHelper {

    private static final String GET_PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid={placeId}&key={apiKey}";

    @Autowired
    private RestTemplate restTemplate;

    @Value(value = "${google.apy-key}")
    private String key;

    public PlaceDetailsExt getPlaceDetails(String placeId) {

        Map<String, Objects> parameters = new HashMap() {
            {
                put("placeId", placeId);
                put("apiKey", key);
            }
        };


        ResponseEntity<PlaceDetailsExt> responseEntity = this.restTemplate.getForEntity(GET_PLACE_DETAILS_URL, PlaceDetailsExt.class, parameters);

        return responseEntity.getBody();
    }
}
