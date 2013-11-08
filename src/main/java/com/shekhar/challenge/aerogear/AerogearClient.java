package com.shekhar.challenge.aerogear;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class AerogearClient {

    private String aerogearServerUrl = System.getenv("AEROGEAR_SERVER_URL");
    private String aerogearApplicationId = System.getenv("AEROGEAR_APPLICATION_ID");
    private String aerogearMasterSecret = System.getenv("AEROGEAR_MASTER_SECRET");

    public String sendMessage(String message) {
        try {
            HttpPost httpPost = new HttpPost(aerogearServerUrl);
            httpPost.addHeader("Content-Type", "application/json");

            String jsonStr = toJson(new Notification(new Message(message)));
            httpPost.setEntity(new StringEntity(jsonStr));

            DefaultHttpClient httpClient = new DefaultHttpClient();

            Credentials credentials = new UsernamePasswordCredentials(aerogearApplicationId, aerogearMasterSecret);
            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            // Log warning
            return null;
        }
    }

    private static <T> String toJson(T obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "Unable to generate json";
        }
    }

}
