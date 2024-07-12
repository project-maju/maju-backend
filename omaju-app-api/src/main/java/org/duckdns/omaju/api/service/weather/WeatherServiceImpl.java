package org.duckdns.omaju.api.service.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.dto.weather.WeatherDTO;
import org.duckdns.omaju.core.util.HTTPUtils;
import org.duckdns.omaju.core.util.JSONUtils;
import org.duckdns.omaju.core.util.network.Get;
import org.duckdns.omaju.core.util.network.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl {
    @Value("${weather.key}")
    private String APP_ID;

    public DataResponseDTO<?> currentWeather(double lat, double lon) throws RuntimeException {
        JsonObject weatherData = requestCurrentWeather(lat, lon);

        WeatherDTO weatherDTO = WeatherDTO.builder()
                .lat(lat)
                .lon(lon)
                .temp(getTemp(weatherData))
                .description(getDescription(weatherData))
                .build();

        return DataResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .statusName(HttpStatus.OK.name())
                .message("날씨 데이터를 성공적으로 조회하였습니다.")
                .data(weatherDTO)
                .build();
    }

    private double getTemp(JsonObject weatherData) {
        return ((JsonObject) weatherData.get("main")).get("temp").getAsBigDecimal().doubleValue();
    }

    private String getDescription(JsonObject weatherData) {
        return ((JsonObject) ((JsonArray) weatherData.get("weather")).get(0)).get("description").toString();
    }

    private JsonObject requestCurrentWeather(double lat, double lng) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lang=kr&units=metric&lat=%f&lon=%f&appid=%s", lat, lng, APP_ID);

        Header header = new Header()
                .append("User-Agent", HTTPUtils.USER_AGENT)
                .append("Accept-Language", HTTPUtils.ACCEPT_LANGUAGE)
                .append("Accept-Encoding", HTTPUtils.ACCEPT_ENCODING)
                .append("Connection", HTTPUtils.CONNECTION);

        try {
            Get get = new Get(url)
                    .setHeader(header)
                    .execute();

            int responseCode = get.getResponseCode();
            if (responseCode != org.apache.http.HttpStatus.SC_OK) {
                log.debug("responseCode: {}", responseCode);
                throw new RuntimeException("통신 오류: " + get.getUrl());
            }

            return JSONUtils.parse(get.getResult());
        } catch(Exception e) {
            e.printStackTrace();
            log.error("날씨 조회 중 통신 오류가 발생했습니다.");
            throw new RuntimeException();
        }
    }
}
