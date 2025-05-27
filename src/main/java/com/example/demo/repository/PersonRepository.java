package com.example.demo.repository;

import com.example.demo.model.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person, Integer> {
    Person michael = new Person(1, "Michael", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");

    default Mono<Person> getById(Integer id) {
        return Mono.just(michael);
    }

    default Flux<Person> findAll() {
        return Flux.just(michael, fiona, sam, jesse);
    }

}
