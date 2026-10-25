
package com.achhecode.browserpilot.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PuzzleServiceClient {

    private final HttpClient httpClient;

    public PuzzleServiceClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String solveWithPinpoint(
            String baseUrl,
            Game game,
            String requestJson) throws IOException, InterruptedException {

        return postJson(
                baseUrl,
                game.getPinpointPath(),
                requestJson);
    }

    public String solveWithLinkedin(
            String baseUrl,
            Game game,
            String requestJson) throws IOException, InterruptedException {

        return postJson(
                baseUrl,
                game.getLinkedinPath(),
                requestJson);
    }

    private String postJson(
            String baseUrl,
            String path,
            String requestJson) throws IOException, InterruptedException {

        String url = normalizeBaseUrl(baseUrl) + path;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException(
                    "HTTP " + statusCode + " from " + url
                            + "\nResponse: " + response.body());
        }

        return response.body();
    }

    private String normalizeBaseUrl(String baseUrl) {
        String normalized = baseUrl.trim();

        while (normalized.endsWith("/")) {
            normalized = normalized.substring(
                    0, normalized.length() - 1);
        }

        if (!normalized.startsWith("http://")
                && !normalized.startsWith("https://")) {
            normalized = "http://" + normalized;
        }

        return normalized;
    }
}
