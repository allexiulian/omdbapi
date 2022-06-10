package util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public final class ConnectionCheck {

    public static final ConnectionCheck ins = new ConnectionCheck();

    public HttpEntity check(CloseableHttpClient httpClient, String search) {

        HttpGet request = new HttpGet("https://www.omdbapi.com/?apikey=53dd328" + search);
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                System.out.println("Bad status code: " + status);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.getEntity();
    }

}
