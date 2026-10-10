package com.natureevent.serviceImpl;

import com.natureevent.service.LocationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

	private final RestTemplate restTemplate;

	public LocationServiceImpl() {
		this.restTemplate = new RestTemplate();
	}

	@Override
	public List<Map<String, Object>> searchLocation(String query) {

		String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
				.queryParam("format", "jsonv2").queryParam("q", query).queryParam("limit", 5).build().encode()
				.toUriString();

		HttpHeaders headers = new HttpHeaders();

		headers.set(HttpHeaders.USER_AGENT, "NatureEvent/1.0 (nature-event-application)");

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, List.class);

		return response.getBody();
	}
}