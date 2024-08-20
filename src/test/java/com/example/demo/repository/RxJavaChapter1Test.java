package com.example.demo.repository;

import com.example.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class RxJavaChapter1Test {

	PersonRepository repository;

	@BeforeEach
	void setUp() {
		repository = new PersonRepository();
	}

	@Test
	void oldWay() {
		List<String> list = Arrays.asList("One12", "Two", "Three", "Four", "Five");
		Iterator<String> iterator = list.listIterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	@Test
	void newWay() {
		List<String> list = Arrays.asList("One", "Two", "Three", "Four", "Five");
		Observable<String> observableList = Observable.from(list);
		observableList.subscribe(new Action1<String>() {
			@Override
			public void call(String s) {
				System.out.println(s);
			}
		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable e) {
				System.out.println("Error Occurred..." + e.getMessage());
			}
		}, new Action0() {
			@Override
			public void call() {
				System.out.println("Completed...");
			}
		});
	}
}