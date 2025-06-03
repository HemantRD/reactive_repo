package com.example.demo.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository
        extends ReactiveCrudRepository<Taco, Long> {
}