package com.example.demo.stream.reactive;

import com.example.demo.stream.reactive.book.CreateObservable;
import com.example.demo.stream.reactive.book.Helpers;
import com.google.gson.Gson;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import rx.Notification;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.apache.http.ObservableHttp;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.blockingSubscribePrint;
import static com.example.demo.stream.reactive.ReactiveProgChap1To4.subscribePrint;

public class ReactiveProgChap5to6 {

    public static void main(String[] args) throws Exception {
        // Using the sample() operator not working as expected
        Path path = Paths.get("src", "main", "resources");
        CountDownLatch latch = new CountDownLatch(7);
        Observable<String> data = CreateObservable.listFolderViaUsing(path, "*")
                .flatMap(file -> {
                    if (!Files.isDirectory(file)) {
                        return CreateObservable.from(file).subscribeOn(Schedulers.io());
                    }
                    return Observable.empty();
                });
        Helpers.subscribePrint(
                data.sample(Observable.interval(
                                        100L, TimeUnit.MILLISECONDS).take(10)
                                .concatWith(Observable.interval(
                                        200L, TimeUnit.MILLISECONDS)))
                        .doOnCompleted(() -> latch.countDown()),
                "sample(Observable)");
        latch.await(15L, TimeUnit.SECONDS);
        Thread.sleep(20000);
    }

    public static void main17(String[] args) throws Exception {
        // we may get rx.exceptions.MissingBackpressureException for large files
        Path path = Paths.get("src", "main", "resources");
        Observable<String> data = CreateObservable.listFolderViaUsing(path, "*")
                .flatMap(file -> {
                    if (!Files.isDirectory(file)) {
                        return CreateObservable.from(file).subscribeOn(Schedulers.io());
                    }
                    return Observable.empty();
                });
        Helpers.subscribePrint(data, "File");
        Thread.sleep(250);
    }

    public static void main16(String[] args) throws Exception {
        // We will request the profiles of the followers in parallel.
        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            CountDownLatch latch = new CountDownLatch(1);
            client.start();
            Observable<Map> response = CreateObservable.requestJson(
                    client,
                    "https://api.github.com/users/meddle0x53/followers"
            );
            response
                    .map(followerJson -> followerJson.get("url"))
                    .cast(String.class)
                    .flatMap(profileUrl -> CreateObservable
                            .requestJson(client, profileUrl)
                            .subscribeOn(Schedulers.io())
                            .filter(res -> res.containsKey("followers"))
                            .map(json -> // (4)
                                    json.get("login") + " : " +
                                            json.get("followers"))
                    )
                    .doOnNext(follower -> System.out.println(follower))
                    .count()
                    .doOnCompleted(() -> latch.countDown())
                    .subscribe(sum -> System.out.println("meddle0x53 : " + sum));
            latch.await();
        }
        Thread.sleep(20000);
    }

    public static void main15(String[] args) throws Exception {
        // Parallelism implementation 5 threads
        Observable<Integer> range = Observable
                .range(20, 5)
                .flatMap(n -> Observable
                        .range(n, 3)
                        .subscribeOn(Schedulers.computation())
                        .doOnEach(debug("Source")));
        range.subscribe();

        Thread.sleep(10000);
    }

    public static void main14(String[] args) throws Exception {
        //both the subscribeOn() and observeOn() operator
        CountDownLatch latch = new CountDownLatch(1);
        Observable<Integer> range = Observable.range(20, 3)
                .subscribeOn(Schedulers.newThread())
                .doOnEach(debug("Source"));

        Observable<Character> chars = range
                .observeOn(Schedulers.io())
                .map(n -> n + 48)
                .doOnEach(debug("+48 ", "   "))
                .observeOn(Schedulers.computation())
                .map(n -> Character.toChars(n))
                .map(c -> c[0])
                .doOnEach(debug("Chars ", "   "))
                .finallyDo(() -> latch.countDown());
        chars.subscribe();
        latch.await();
    }

    public static void main13(String[] args) throws Exception {
        // observeOn operator it executes the part of the chain from its place within it, onwards.
        CountDownLatch latch = new CountDownLatch(1);
        Observable<Integer> range = Observable
                .range(20, 3)
                .doOnEach(debug("Source"));

        Observable<Character> chars = range
                .map(n -> n + 48)
                .doOnEach(debug("+48", "   "))
                .map(n -> Character.toChars(n))
                .map(c -> c[0])
                .observeOn(Schedulers.computation())
                .doOnEach(debug("Chars ", "   "))
                .finallyDo(() -> latch.countDown());
        chars.subscribe();
        System.out.println("Hey!");
        latch.await();
    }

    public static void main12(String[] args) throws Exception {
        //but our code will be executed on the computation scheduler because this is specified first in the chain.
        CountDownLatch latch = new CountDownLatch(1);
        Observable<Integer> range = Observable.range(20, 3)
                .doOnEach(debug("Source"))
                .subscribeOn(Schedulers.computation());

        Observable<Character> chars = range.map(n -> n + 48)
                .map(n -> Character.toChars(n))
                .subscribeOn(Schedulers.io()).map(c -> c[0]).subscribeOn(Schedulers.newThread())
                .doOnEach(debug("Chars ", "   "))
                .finallyDo(() -> latch.countDown());

        chars.subscribe();
        latch.await();
    }

    public static void main11(String[] args) throws Exception {
        // everything happens on main thread
        Observable<Integer> range = Observable.range(20, 4).doOnEach(debug("Source"));
        range.subscribe();

        System.out.println("Hey!");
        System.out.println("\n\n\n");
        // everything happens on computation thread (other thread)
        CountDownLatch latch = new CountDownLatch(1);
        Observable<Integer> range2 = Observable.range(20, 4).doOnEach(debug("Source")).subscribeOn(Schedulers.computation()).
                finallyDo(() -> latch.countDown());
        range2.subscribe();
        System.out.println("Hey!");
        latch.await();
    }

    public static void main10(String[] args) throws Exception {
        // everything runs on main thread. both having same output
        schedule(Schedulers.immediate(), 2, false);
        schedule(Schedulers.immediate(), 2, true);

        System.out.println("\n\n\n");
        // trampoline method enqueues sub-tasks on the current thread. not clear to me
        schedule(Schedulers.trampoline(), 2, false);
        schedule(Schedulers.trampoline(), 2, true);

        //Schedulers.newThread
        System.out.println("\n\n\n");
        schedule(Schedulers.newThread(), 2, true);
        schedule(Schedulers.newThread(), 2, false);

        //Schedulers.computation
        System.out.println("\n\n\n");
        schedule(Schedulers.computation(), 5, false);

        //Schedulers.io()
        System.out.println("\n\n\n");
        schedule(Schedulers.io(), 2, false);

        Thread.sleep(10000);
    }

    static void schedule(Scheduler scheduler, int numberOfSubTasks, boolean onTheSameWorker) {
        List<Integer> list = new ArrayList<>();
        AtomicInteger current = new AtomicInteger();
        Random random = new Random();
        Scheduler.Worker worker = scheduler.createWorker();
        Action0 addWork = () -> {
            synchronized (current) {
                System.out.println("  Add : " + Thread.currentThread().getName() + " " + current.get());
                list.add(random.nextInt(current.get()));
                System.out.println("  End add : " + Thread.currentThread().getName() + " " + current.get());
            }
        };
        Action0 removeWork = () -> {
            synchronized (current) {
                if (!list.isEmpty()) {
                    System.out.println("  Remove : " + Thread.currentThread().getName());
                    list.remove(0);
                    System.out.println("  End remove :" + Thread.currentThread().getName());
                }
            }
        };

        Action0 work = () -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 1; i <= numberOfSubTasks; i++) {
                current.set(i);
                System.out.println("Begin add!");
                if (onTheSameWorker) {
                    worker.schedule(addWork);
                } else {
                    scheduler.createWorker().schedule(addWork);
                }
                System.out.println("End add!");
            }
            while (!list.isEmpty()) {
                System.out.println("Begin remove!");
                if (onTheSameWorker) {
                    worker.schedule(removeWork);
                } else {
                    scheduler.createWorker().schedule(removeWork);
                }
                System.out.println("End remove!");
            }
        };
        worker.schedule(work);
    }

    public static void main9(String[] args) {
        // this runs on the main thread
        Observable.range(5, 5).doOnEach(debug("Test", "")).subscribe();
        // this runs on the separate computation scheduler's thread
        System.out.println("\n\n\n");
        CountDownLatch latch = new CountDownLatch(1);
        Observable.interval(500L, TimeUnit.MILLISECONDS).take(5)
                .finallyDo(() -> latch.countDown()).doOnEach(debug("Default interval")).subscribe();
        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        // this runs on main thread
        System.out.println("\n\n\n");
        CountDownLatch latch1 = new CountDownLatch(1);
        Observable.interval(500L, TimeUnit.MILLISECONDS, Schedulers.immediate()).take(5)
                .finallyDo(() -> latch1.countDown()).doOnEach(debug("Immediate interval")).subscribe();
        try {
            latch1.await();
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

    private static Map<String, Set<Map<String, Object>>> cache = new ConcurrentHashMap<>();

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