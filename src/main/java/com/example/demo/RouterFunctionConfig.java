package com.example.demo;

import com.example.demo.model.Person;
import com.example.demo.r2dbc.OrderRepository;
import com.example.demo.r2dbc.Taco;
import com.example.demo.r2dbc.TacoOrder;
import com.example.demo.r2dbc.TacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

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
    private TacoRepository tacoRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/api/tacos").
                        and(queryParam("recent", t -> t != null)),
                this::recents)
                .andRoute(POST("/api/tacos"), this::postTaco)
                .andRoute(POST("/api/saveTacos"), req -> {
                    return saveTacoOrder(req);
                });
    }

    public Mono<ServerResponse> recents(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepo.findAll().take(12), Person.class);
    }

    public Mono<ServerResponse> postTaco(ServerRequest request) {
        return request.bodyToMono(Taco.class)
                .flatMap(taco -> tacoRepo.save(taco))
                .flatMap(savedTaco -> {
                    return ServerResponse
                            .created(URI.create(
                                    "http://localhost:8080/api/tacos/" +
                                            savedTaco.getId()))
                            .bodyValue(savedTaco);
                });
    }

    // Nice code
    public Mono<ServerResponse> saveTacoOrder(ServerRequest request) {
        return request.bodyToMono(TacoOrder.class)
                .flatMap(order -> {
                    List<Taco> tacos = order.getTacos();
                    order.setTacos(new ArrayList<>());
                    return tacoRepo.saveAll(tacos).map(taco -> {
                        order.addTaco(taco);
                        return order;
                    }).last();
                }).flatMap(tacoOrder -> {
                    return orderRepo.save(tacoOrder).flatMap(r -> {
                        return ServerResponse.ok().bodyValue(tacoOrder);
                    });
                });
    }

}