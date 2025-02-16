package org.duckdns.omaju.api.service.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.dto.weather.WeatherResponseDTO;
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
public class WeatherServiceImpl implements WeatherService {
    @Value("${weather.key}")
    private String WEATHER_APP_ID;
    @Value("${tmap.key}")
    private String TMAP_API_KEY;
    private final String VERSION = "1";
    private final String CALLBACK = "application/json";

    public DataResponseDTO<?> currentWeather(double lat, double lon) throws RuntimeException {
        JsonObject weatherData = requestCurrentWeather(lat, lon);

        String description; // 맑음, 흐림, 비, 눈
        switch(getIcon(weatherData)) {
            case "01d":
            case "01n":
            case "02d":
            case "02n":
                description = "맑음";
                break;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
            case "50d":
            case "50n":
                description = "흐림";
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
            case "11d":
            case "11n":
                description = "비";
                break;
            case "13d":
            case "13n":
                description = "눈";
            default:
                description = "맑음";
                break;
        }

        WeatherResponseDTO weatherDTO = WeatherResponseDTO.builder()
                .lat(lat)
                .lon(lon)
                .temp(getTemp(weatherData))
                .description(description)
                .build();

        return DataResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .statusName(HttpStatus.OK.name())
                .message("날씨 데이터를 성공적으로 조회하였습니다.")
                .data(weatherDTO)
                .build();
    }

    @Override
    public DataResponseDTO<?> currentAddress(double lat, double lon) throws RuntimeException {
        JsonObject addressData = requestCurrentAddress(lat, lon);

        String cityDo = getCityDo(addressData.get("addressInfo").getAsJsonObject());
        String guGun = getGuGun(addressData.get("addressInfo").getAsJsonObject());

        return DataResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .statusName(HttpStatus.OK.name())
                .message("주소 데이터를 성공적으로 조회하였습니다.")
                .data(cityDo + " " + guGun)
                .build();
    }

    private double getTemp(JsonObject weatherData) {
        return ((JsonObject) weatherData.get("main")).get("temp").getAsBigDecimal().doubleValue();
    }

    private String getIcon(JsonObject weatherData) {
        return ((JsonObject) ((JsonArray) weatherData.get("weather")).get(0)).get("icon").getAsString();
    }

    private JsonObject requestCurrentWeather(double lat, double lon) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lang=kr&units=metric&lat=%f&lon=%f&appid=%s", lat, lon, WEATHER_APP_ID);

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

    private JsonObject requestCurrentAddress(double lat, double lon) {
        String url = String.format("https://apis.openapi.sk.com/tmap/geo/reversegeocoding?lat=%s&lon=%s&version=%s&format=json&callback=%s&appKey=%s", lat, lon, VERSION, CALLBACK, TMAP_API_KEY);
        System.out.println(url);
        Header header = new Header()
                .append("User-Agent", HTTPUtils.USER_AGENT)
                .append("Accept-Language", HTTPUtils.ACCEPT_LANGUAGE)
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
            log.error("주소 조회 중 오류가 발생했습니다.");
            throw new RuntimeException();
        }
    }

    private String getCityDo(JsonObject addressInfo) {
        return addressInfo.get("city_do").getAsString();
    }

    private String getGuGun(JsonObject addressInfo) {
        return addressInfo.get("gu_gun").getAsString();
    }
}
