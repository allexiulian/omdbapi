package util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public final class ConnectionCheck {

    public static final ConnectionCheck ins = new ConnectionCheck();
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;

    public int checkStatus(StringBuilder search) throws IOException {
        HttpGet request = new HttpGet("https://www.omdbapi.com/?apikey=53dd328" + search);
        response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode();
    }

    public HttpEntity getEntity() {
        return this.response.getEntity();
    }
}
