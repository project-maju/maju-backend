package org.duckdns.omaju.core.util.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Header {
    private Map<String, String> headers = new HashMap<>();
    private List <NameValuePair> params = new ArrayList<>();

    public Header append(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String getValue(String key) {
        return headers.get(key);
    }

    public List<String> getKeyList() {
        List<String> keyList = new ArrayList<>();
        headers.keySet().forEach(key -> keyList.add(key));

        return keyList;
    }

    public Header appendParam(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        return this;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public Header copy() {
        Header header = new Header();
        headers.keySet().forEach(key -> header.append(key, headers.get(key)));

        return header;
    }
}
