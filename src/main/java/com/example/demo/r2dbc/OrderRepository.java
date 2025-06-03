package com.example.demo.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository
        extends ReactiveCrudRepository<TacoOrder, Long> {
}