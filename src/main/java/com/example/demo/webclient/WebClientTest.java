package com.example.demo.webclient;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientTest {

    public static void main(String[] args) {
        Mono<String> ingredient = WebClient.create()
                .get()
                .uri("http://localhost:8080/api/ingredients")
                .retrieve()
                .bodyToMono(String.class);
        ingredient.subscribe(i -> {
            System.out.println(i);
        });
    }
}
