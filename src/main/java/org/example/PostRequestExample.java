package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostRequestExample {
    public static void main(String[] args) throws Exception {
        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // JSON body
        String json = """
        {
            "title": "foo",
            "body": "bar",
            "userId": 1
        }
        """;

        // Create POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print raw response
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());

        // Parse JSON response using Jackson
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.body());

        // Basic tests (like Postman)
        if (response.statusCode() == 201) {
            System.out.println("✅ Status code is 201");
        } else {
            System.out.println("❌ Unexpected status: " + response.statusCode());
        }

        if (jsonResponse.has("id") && jsonResponse.get("id").isInt()) {
            System.out.println("✅ ID exists and is numeric: " + jsonResponse.get("id"));
        }

        if ("foo".equals(jsonResponse.get("title").asText())) {
            System.out.println("✅ Title matches expected");
        } else {
            System.out.println("❌ Title mismatch");
        }
    }
}
