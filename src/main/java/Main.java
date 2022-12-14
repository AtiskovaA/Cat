import com.google.gson.Gson;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        String newJson;
        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create()
                        .setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectTimeout(5000)
                                .setSocketTimeout(30000)
                                .setRedirectsEnabled(false)
                                .build())
                        .build();
        ) {
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
            CloseableHttpResponse response = httpClient.execute(request);
            newJson = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        AboutCat[] facts = gson.fromJson(newJson, AboutCat[].class);
        List<String> textFacts = (List<String>) Arrays.stream(facts)
                .filter(aboutCat -> aboutCat.getUpvotes() != null && aboutCat.getUpvotes() > 0)
                .map(aboutCat -> aboutCat.getText())
                .collect(toList());

        for (String text : textFacts) {
            System.out.println(text);
        }
    }
}
