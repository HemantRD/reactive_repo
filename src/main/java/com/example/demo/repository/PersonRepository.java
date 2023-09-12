package com.example.demo.repository;

import com.example.demo.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepository {
    Person michael = new Person(1, "Michael", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");

    public Mono<Person> getById(Integer id) {
        return Mono.just(michael);
    }

    public Flux<Person> findAll() {
        return Flux.just(michael, fiona, sam, jesse);
    }

}
