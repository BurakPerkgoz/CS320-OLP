package olp.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AI {

    public static String askAI(String major, String credits, String semester, String extra) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String jsonBody = """
        {
          "major": "%s",
          "credits": "%s",
          "semester": "%s",
          "extra": "%s"
        }
        """.formatted(major, credits, semester, extra);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://45.141.150.174:5657/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
