package com.example.demo.stream.reactive;

import com.google.gson.Gson;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.apache.http.ObservableHttp;
import rx.functions.Action1;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.blockingSubscribePrint;
import static com.example.demo.stream.reactive.ReactiveProgChap1To4.subscribePrint;

public class ReactiveProgChap5to6 {

    private static Map<String, Set<Map<String, Object>>> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // this runs on the main thread
        Observable.range(5, 5).doOnEach(debug("Test", "")).subscribe();
        // this runs on the separate computation scheduler's thread
        System.out.println("\n\n\n");
        CountDownLatch latch = new CountDownLatch(1);
        Observable.interval(500L, TimeUnit.MILLISECONDS).take(5).finallyDo(() -> latch.countDown()).doOnEach(debug("Default interval")).subscribe();
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    static <T> Action1<Notification<? super T>> debug(String description, String offset) {
        AtomicReference<String> nextOffset = new AtomicReference<>(">");
        return (Notification<? super T> notification) -> {
            switch (notification.getKind()) {
                case OnNext:
                    System.out.println(Thread.currentThread().getName() + "|" + description + ": " + offset +
                            nextOffset.get() + notification.getValue());
                    break;
                case OnError:
                    System.err.println(Thread.currentThread().getName() + "|" + description + ": " + offset +
                            nextOffset.get() + " X " + notification.getThrowable());
                    break;
                case OnCompleted:
                    System.out.println(Thread.currentThread().getName() + "|" + description + ": " + offset + nextOffset.get() + "|");
                default:
                    break;
            }
            nextOffset.getAndUpdate(p -> "-" + p);
        };
    }

    static <T> Action1<Notification<? super T>> debug(String description) {
        return debug(description, "");
    }


    public static void main8(String[] args) throws Exception {
        //complete code to call the endpoint https://github.com/meddle0x53 with filter not forked (not working)
        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            String username = "meddle0x53";
            Observable<Map> resp = githubUserInfoRequest(client, username);
            blockingSubscribePrint(resp.map(json -> json.get("name") + "(" + json.get("language") + ")"), "Json");
        }
    }

    static Observable<Map> githubUserInfoRequest(HttpAsyncClient client, String githubUser) {
        if (githubUser == null) {
            return Observable.<Map>error(new NullPointerException("Github user must not be null"));
        }
        String url = "https://api.github.com/users/" + githubUser + "/repos";
        return requestJson(client, url)
                .filter(json -> json.containsKey("git_url"))
                .filter(json -> json.get("fork").equals(false));
    }

    static Observable<Map> requestJson(HttpAsyncClient client, String url) {
        Observable<String> rawResponse = ObservableHttp.createGet(url, client).toObservable()
                .flatMap(resp -> resp.getContent()
                        .map(bytes -> new String(bytes, StandardCharsets.UTF_8)))
                .retry(5)
                .cast(String.class)
                .map(String::trim)
                .doOnNext(resp -> getCache(url).clear());

        Observable<String> objects = rawResponse.filter(data -> data.startsWith("{")).map(data -> "[" + data + "]");
        Observable<String> arrays = rawResponse.filter(data -> data.startsWith("["));

        Observable<Map> response = arrays.ambWith(objects).map(data -> {
                    return new Gson().fromJson(data, List.class);
                }).flatMapIterable(list -> list)
                .cast(Map.class)
                .doOnNext(json -> getCache(url).add((Map<String, Object>) json));
        return Observable.amb(fromCache(url), response);
    }

    public static Set<Map<String, Object>> getCache(String url) {
        if (!cache.containsKey(url)) {
            cache.put(url, new HashSet<Map<String, Object>>());
        }
        return cache.get(url);
    }

    public static Observable<Map<String, Object>> fromCache(String url) {
        return Observable.from(getCache(url)).defaultIfEmpty(null)
                .flatMap(json -> json == null ? Observable.never() : Observable.just(json))
                .doOnNext(json -> json.put("json_cached", true));
    }


    public static void main7(String[] args) throws Exception {
        //retry operator
        subscribePrint(Observable.create(new ErrorEmitter()).retry(), "Error Retry");
        System.out.println("\n\n\n");

        //retryWhen operator
        Observable<Integer> when = Observable.create(new ErrorEmitter())
                .retryWhen(attempts -> {
                    return attempts.flatMap(error -> {
                        if (error instanceof FooException) {
                            System.err.println("Delaying...");
                            return Observable.timer(2L, TimeUnit.SECONDS);
                        }
                        return Observable.error(error);
                    });
                })
                .retry((attempts, error) -> {
                    return error instanceof BooException && attempts < 3;
                });
        subscribePrint(when, "retryWhen");
        Thread.sleep(25000);
    }

    static class FooException extends RuntimeException {
        public FooException() {
            super("Foo!");
        }
    }

    static class BooException extends RuntimeException {
        public BooException() {
            super("Boo!");
        }
    }

    static class ErrorEmitter implements Observable.OnSubscribe<Integer> {
        private int throwAnErrorCounter = 5;

        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            subscriber.onNext(1);
            subscriber.onNext(2);
            if (throwAnErrorCounter > 4) {
                throwAnErrorCounter--;
                subscriber.onError(new FooException());
                return;
            }
            if (throwAnErrorCounter > 0) {
                throwAnErrorCounter--;
                subscriber.onError(new BooException());
                return;
            }
            subscriber.onNext(3);
            subscriber.onNext(4);
            subscriber.onCompleted();
        }
    }

    public static void main6(String[] args) {
        //defaultIfEmpty operator
        Observable<Object> test = Observable.empty().defaultIfEmpty(5);
        subscribePrint(test, "defaultIfEmpty");

        System.out.println("\n\n\n");
        //onErrorReturn operator
        Observable<Integer> numbers = Observable.just("1", "2", "three", "4", "5").map(Integer::parseInt).onErrorReturn(r -> -1);
        subscribePrint(numbers, "onErrorReturn");

        System.out.println("\n\n\n");
        //onExceptionResumeNext operator
        Observable<Integer> defaultOnError = Observable.just(5, 4, 3, 2, 1);
        Observable<Integer> numbers2 = Observable.just("1", "2", "three", "4", "5").map(Integer::parseInt)
                .onExceptionResumeNext(defaultOnError);
        subscribePrint(numbers2, "onExceptionResumeNext");

        System.out.println("\n\n\n");
        //onErrorResumeNext operator
        Observable<Integer> numbers3 = Observable.just("1", "2", "three", "4", "5").doOnNext(number -> {
                    assert !numbers.equals("three");
                }
        ).map(Integer::parseInt).onErrorResumeNext(defaultOnError);
        subscribePrint(numbers3, "onErrorResumeNext");

    }

    public static void main5(String[] args) {
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