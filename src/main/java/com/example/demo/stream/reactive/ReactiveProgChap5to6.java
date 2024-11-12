package com.example.demo.stream.reactive;

import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.*;

public class ReactiveProgChap5to6 {

    public static void main(String[] args) {
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