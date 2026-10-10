package com.natureevent.service;

import java.util.List;
import java.util.Map;

public interface LocationService {

	List<Map<String, Object>> searchLocation(String query);
}