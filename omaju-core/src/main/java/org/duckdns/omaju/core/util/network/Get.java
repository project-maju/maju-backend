package org.duckdns.omaju.core.util.network;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class Get {
    HttpGet httpGet;
    CloseableHttpClient httpclient;
    HttpResponse response;
    String url;
    Header header;
    Charset charset = Charset.forName("UTF-8");

    public Get(String url) throws IOException {
        this.url = url;
        this.httpclient = HttpClientBuilder.create().build();
        this.httpGet = new HttpGet(url);
    }

    public Get(String url, Header header) throws IOException {
        this.url = url;
        this.httpclient = HttpClientBuilder.create().build();
        this.httpGet = new HttpGet(url);

        setHeader(header);
    }

    public String getUrl() {
        return this.url;
    }

    public Get setHeader(Header header) {
        this.header = header;
        return this;
    }

    public Get execute() throws IOException {
        if (header == null) {
            this.header = new Header();
        }

        List<String> keyList = header.getKeyList();
        for (String key: keyList) {
            httpGet.addHeader(key, header.getValue(key));
        }

        response = httpclient.execute(httpGet);
        return this;
    }

    public int getResponseCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getReasonPhrase() {
        return response.getStatusLine().getReasonPhrase();
    }

    public String getResult() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset))) {
            String inputLine;
            StringBuffer sb = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
