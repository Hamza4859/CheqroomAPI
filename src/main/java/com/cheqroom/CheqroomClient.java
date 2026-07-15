package com.cheqroom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class CheqroomClient {

    private final String baseUrl;
    private final String token;
    private final HttpClient http;
    private final ObjectMapper mapper;

    public CheqroomClient(CheqroomConfig config) {
        // ADDED: A trailing slash '/' right after 'jwt'
        this.baseUrl = "https://api.cheqroom.com/api/latest/" + config.getUserId() + "/null/jwt/";
        this.token = config.getToken();
        this.http = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }   

    public JsonNode post(String path, Map<String, String> params) {
        String body = params.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(request);
    }


    public JsonNode postJson(String path, Object payload) {
        String body;
        try {
            body = mapper.writeValueAsString(payload);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(request);
    }

    private JsonNode send(HttpRequest request) {
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("→ " + request.method() + " " + request.uri());
            System.out.println("← HTTP " + response.statusCode());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
            }

            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    public ObjectMapper getMapper() { return mapper; }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public JsonNode get(String path) {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + path))
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/json")
            .GET()
            .build();

    return send(request);
    }
}