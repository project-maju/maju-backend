package org.duckdns.omaju.core.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONUtils {
    public static JsonObject parse(String json) {
        try {
            JsonParser jsonParser = new JsonParser();
            return (JsonObject) jsonParser.parse(json);
        } catch(Exception e) {
            throw new RuntimeException("JSON 파싱 오류발생", e);
        }
    }
}
