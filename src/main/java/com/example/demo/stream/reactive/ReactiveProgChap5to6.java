package com.example.demo.stream.reactive;

import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.blockingSubscribePrint;
import static com.example.demo.stream.reactive.ReactiveProgChap1To4.subscribePrint;

public class ReactiveProgChap5to6 {

    public static void main(String[] args) {
        // simple zip
        Observable<Integer> zip = Observable.zip(Observable.just(1, 2),
                Observable.just(5, 2, 6, 5),
                Observable.just(1, 2),
                (a, b, v) -> a + b + v
        );
        subscribePrint(zip, "Simple zip");
        System.out.println("\n\n\n");

        // complex zip
        Observable<String> timedZip = Observable.zip(
                Observable.from(Arrays.asList("Z", "I", "P", "P")),
                Observable.interval(900L, TimeUnit.MILLISECONDS),
                (value, i) -> value
        );
        blockingSubscribePrint(timedZip, "timedZip");
    }

}