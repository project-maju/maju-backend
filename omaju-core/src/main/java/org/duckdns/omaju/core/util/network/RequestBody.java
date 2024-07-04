package org.duckdns.omaju.core.util.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class RequestBody {
    private JsonObject requestBody;

    public RequestBody() {
        this.requestBody = new JsonObject();
    }

    public RequestBody(JsonObject requestBody) {
        this.requestBody = requestBody;
    }

    public RequestBody append(String key, JsonElement value) {
        this.requestBody.add(key, value);
        return this;
    }

    public JsonElement getValue(String key) {
        return this.requestBody.get(key);
    }

    public List<String> getKeyList() {
        List<String> keyList = new ArrayList<>();
        this.requestBody.keySet().forEach(key -> keyList.add(key));

        return keyList;
    }

    public RequestBody copy() {
        return new RequestBody(getRequestBody().deepCopy());
    }

    public JsonObject getRequestBody() {
        return this.requestBody;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "requestBody=" + this.requestBody +
                '}';
    }
}
