package org.example;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public class Task4 {
    final static HttpClient client = HttpClient.newHttpClient();

    private static HttpResponse<String> MakeRequest(String URL) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    public static void CompleteTask()
    {
        var response = MakeRequest("https://httpbin.org/headers");
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(response.body());
        JsonNode headers = root.get("headers");
        StringJoiner namesJoiner = new StringJoiner(", ");
        for(var propertyName : headers.propertyNames())
            namesJoiner.add(propertyName);
        System.out.println(namesJoiner);
    }
}
