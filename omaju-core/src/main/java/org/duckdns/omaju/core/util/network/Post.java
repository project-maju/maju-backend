package org.duckdns.omaju.core.util.network;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.duckdns.omaju.core.util.HTTPUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Post {
    HttpPost httpPost;
    CloseableHttpClient httpclient;
    HttpResponse response;
    String url;
    Header header;
    RequestBody requestBody;
    Charset charset = StandardCharsets.UTF_8;

    public Post(String url) throws IOException {
        this.url = url;
        this.httpclient = HttpClientBuilder.create().build();
        this.httpPost = new HttpPost(url);
    }

    public Post(String url, Header header, RequestBody requestBody) throws IOException {
        this.url = url;
        this.httpclient = HttpClientBuilder.create().build();
        this.httpPost = new HttpPost(url);

        setHeader(header);
        setRequestBody(requestBody);
    }

    public String getUrl() {
        return this.url;
    }

    public Post setHeader(Header header) {
        this.header = header;
        return this;
    }

    public Post setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public Post execute() throws IOException {
        if (header == null) {
            this.header = new Header();
        }

        if (header.getParams().size() > 0) {
            // set params
            header.append("Content-Type", HTTPUtils.CONTENT_TYPE_URLENCODED);
            httpPost.setEntity(new UrlEncodedFormEntity(header.getParams(), HTTP.UTF_8));
        } else {
            // set requestBody
            if (requestBody == null) {
                this.requestBody = new RequestBody();
            }

            StringEntity requestEntity = new StringEntity(requestBody.getRequestBody().toString(), HTTP.UTF_8);
            httpPost.setEntity(requestEntity);
        }

        // set header
        List<String> keyList = header.getKeyList();
        for (String key: keyList) {
            httpPost.addHeader(key, header.getValue(key));
        }

        response = httpclient.execute(httpPost);
        return this;
    }

    public int getResponseCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getReasonPhrase() {
        return response.getStatusLine().getReasonPhrase();
    }

    public String getResult() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset))) {
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

    public InputStream getResultInputStream() {
        try {
            if (this.getResponseCode() == HttpStatus.SC_OK) {
                return response.getEntity().getContent();
            }

            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
