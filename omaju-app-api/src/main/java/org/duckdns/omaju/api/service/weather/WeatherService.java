package org.duckdns.omaju.api.service.weather;

import org.duckdns.omaju.api.dto.response.DataResponseDTO;

public interface WeatherService {
    DataResponseDTO<?> currentWeather(double lat, double lon) throws RuntimeException;
    DataResponseDTO<?> currentAddress(double lat, double lon) throws RuntimeException;
}