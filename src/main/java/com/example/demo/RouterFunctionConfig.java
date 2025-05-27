package com.example.demo;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web
        .reactive.function.server.RouterFunctions.route;
import static org.springframework.web
        .reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"),
                request -> ok().body(just("Hello World! Reactive"), String.class))
                .andRoute(GET("/bye"),
                        request -> ok().body(just("See ya!"), String.class));
    }


    @Autowired
    private PersonRepository tacoRepo;

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/api/tacos").
                        and(queryParam("recent", t -> t != null)),
                this::recents)
                .andRoute(POST("/api/tacos"), this::postTaco);
    }

    public Mono<ServerResponse> recents(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepo.findAll().take(12), Person.class);
    }

    public Mono<ServerResponse> postTaco(ServerRequest request) {
        return request.bodyToMono(Person.class)
                .flatMap(taco -> tacoRepo.save(taco))
                .flatMap(savedTaco -> {
                    return ServerResponse
                            .created(URI.create(
                                    "http:/ /localhost:8080/api/tacos/" +
                                            savedTaco.getId()))
                            .body(savedTaco, Person.class);
                });
    }

}