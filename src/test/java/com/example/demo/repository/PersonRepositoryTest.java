package com.example.demo.repository;

import com.example.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class PersonRepositoryTest {

    PersonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PersonRepository();
    }

    @Test
    void getById() {
        Person person = repository.getById(1).block();
        System.out.println(person.getFirstName());
    }

    @Test
    void getByIdSubcribe() {
        repository.getById(1).subscribe(p -> {
            System.out.println(p.getFirstName());
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = repository.getById(1);

        personMono.map(r -> {
            System.out.println(r.getFirstName());
            return r.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(firstName + " from map");
        });
    }

    @Test
    void findAll() {
    }
}