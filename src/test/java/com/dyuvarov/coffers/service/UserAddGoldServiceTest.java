package com.dyuvarov.coffers.service;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


class UserAddGoldServiceTest {


    @Test
    void addGoldToClan_shouldAddCorrectSumInMultithreading() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/coffers/api/useraddgold?userId=1&clanId=3&gold=10"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpRequest taskRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/coffers/api/task/complete?taskId=1&clanId=3"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<?>> l = new LinkedList<>();
        for (int i = 0; i < 100; ++i) {
            l.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }
        for (int i = 0; i < 100; ++i) {
            l.add(client.sendAsync(taskRequest, HttpResponse.BodyHandlers.ofString()));
        }
        for (CompletableFuture<?> cf : l) {
            cf.join();
        }
    }
}