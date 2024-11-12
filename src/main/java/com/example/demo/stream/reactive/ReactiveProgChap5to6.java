package com.example.demo.stream.reactive;

import rx.Observable;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.*;
import static com.example.demo.stream.reactive.ReactiveProgChap1To4.blockingSubscribePrint;

public class ReactiveProgChap5to6 {

    public static void main(String[] args) {
        Observable<String> words = Observable.just("one", "way", "or", "another", "I'll", "learn", "RxJava")
                .zipWith(Observable.interval(200L, TimeUnit.MILLISECONDS),
                        (x, y) -> x);
        Observable<Long> interval = Observable.interval(500L, TimeUnit.MILLISECONDS);
        //takeUntil operator
        blockingSubscribePrint(words.takeUntil(interval), "takeUntil");
        System.out.println("\n\n\n");

        //takeWhile operator
        blockingSubscribePrint(words.takeWhile(word -> word.length() > 2), "takeWhile");
        System.out.println("\n\n\n");

        //skipUntil operator
        blockingSubscribePrint(words.skipUntil(interval), "skipUntil");
    }

    public static void main4(String[] args) {
        // amb operator
        Observable<String> words = Observable.just("Some", "Other");
        Observable<Long> interval = Observable
                .interval(500L, TimeUnit.MILLISECONDS).take(2);

        Observable<? extends Object> amb = Observable.amb(words, interval);
        blockingSubscribePrint(amb, "Amb 1");

        System.out.println("\n\n\n");
        // amb operator
        Random r = new Random();
        Observable<String> source1 = Observable.just("data from source 1").delay(r.nextInt(1000), TimeUnit.MILLISECONDS);
        Observable<String> source2 = Observable.just("data from source 2").delay(r.nextInt(1000), TimeUnit.MILLISECONDS);
        blockingSubscribePrint(Observable.amb(source1, source2), "Amb 2");
    }

    public static void main3(String[] args) {
        Observable<String> greetings = Observable.just("Hello", "Hi", "Howdy",
                        "Zdravei", "Yo", "Good to see ya")
                .zipWith(Observable.interval(1L, TimeUnit.SECONDS), ReactiveProgChap5to6::onlyFirstArg);
        Observable<String> names = Observable.just("Meddle", "Tanya", "Dali", "Joshua")
                .zipWith(Observable.interval(1500L, TimeUnit.MILLISECONDS),
                        ReactiveProgChap5to6::onlyFirstArg);
        Observable<String> punctuations = Observable.just(".", "?", "!", "!!!", "...").zipWith(
                Observable.interval(1100L, TimeUnit.MILLISECONDS),
                ReactiveProgChap5to6::onlyFirstArg);

        //startWith operator similar to concat
        Observable<String> concat = punctuations.startWith(names).startWith(greetings);
        blockingSubscribePrint(concat, "startsWith");

        //concatWith operator similar to concat
        Observable<String> concat1 = greetings.concatWith(names).concatWith(punctuations);
        blockingSubscribePrint(concat1, "concatWith");

    }

    public static void main2(String[] args) {
        //combineLatest example
        Observable<String> greetings = Observable.just("Hello", "Hi", "Howdy",
                        "Zdravei", "Yo", "Good to see ya")
                .zipWith(Observable.interval(1L, TimeUnit.SECONDS), ReactiveProgChap5to6::onlyFirstArg);
        Observable<String> names = Observable.just("Meddle", "Tanya", "Dali", "Joshua")
                .zipWith(Observable.interval(1500L, TimeUnit.MILLISECONDS),
                        ReactiveProgChap5to6::onlyFirstArg);
        Observable<String> punctuations = Observable.just(".", "?", "!", "!!!", "...").zipWith(
                Observable.interval(1100L, TimeUnit.MILLISECONDS),
                ReactiveProgChap5to6::onlyFirstArg);

        Observable<String> combined = Observable.combineLatest(greetings, names, punctuations, (greeting, name, punctuation) -> {
            return greeting + " " + name + punctuation;
        });
        blockingSubscribePrint(combined, "Sentences");

        System.out.println("\n\n\n");
        //merge operator
        Observable<String> merged = Observable.merge(greetings, names, punctuations);
        blockingSubscribePrint(merged, "Words");

        System.out.println("\n\n\n");
        //merge operator
        Observable<String> mergeDelayError = Observable.mergeDelayError(greetings, names, punctuations);
        blockingSubscribePrint(merged, "mergeDelayError");

        System.out.println("\n\n\n");
        //contact operator
        Observable<String> concat = Observable.concat(greetings, names, punctuations);
        blockingSubscribePrint(concat, "concat Operator");
    }

    public static <T, R> T onlyFirstArg(T arg1, R arg2) {
        return arg1;
    }

    public static void main1(String[] args) {
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
        System.out.println("\n\n\n");

        //zipWith operator
        Observable<String> timedZip2 = Observable.from(Arrays.asList("Z", "I", "P", "P")).zipWith(
                Observable.interval(600L, TimeUnit.MILLISECONDS),
                (value, skip) -> value
        );
        blockingSubscribePrint(timedZip2, "Timed Zip2");
    }

}