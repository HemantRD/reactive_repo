package com.example.demo.repository;

import com.example.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    void fluxTestBlockFirst() {
        Person personFlux = repository.findAll().blockFirst();
        System.out.println(personFlux.getFirstName());
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = repository.findAll();
        personFlux.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxToMonoList() {
        Flux<Person> personFlux = repository.findAll();
        Mono<List<Person>> personMonoList = personFlux.collectList();
        personMonoList.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person);
            });
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 3;
        Mono<Person> personMono = personFlux.filter(r -> r.getId() == id).next();
        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 85;
        Mono<Person> personMono = personFlux.filter(r -> r.getId() == id).next();
        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 85;
        Mono<Person> personMono = personFlux.filter(r -> r.getId() == id).single();
        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }
}