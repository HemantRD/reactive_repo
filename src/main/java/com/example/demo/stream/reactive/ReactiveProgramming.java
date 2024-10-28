package com.example.demo.stream.reactive;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ReactiveProgramming {

    public static void main(String[] args) throws Exception {
        Observable<Object> obs = Observable.interval(40l, TimeUnit.MILLISECONDS)
                .switchMap(v -> Observable.timer(0L, 10L, TimeUnit.MILLISECONDS)
                        .map(u -> "Observable <" + (v + 1) + "> : " + (v + u)));
        subscribePrint(obs, "switchMap");
        Thread.sleep(1000);
    }

    public static void main18(String[] args) {
        // flatMapIterable example
        Observable<?> filterableMapped = Observable.just(Arrays.asList(2, 4),
                Arrays.asList("two", "four")).flatMapIterable(l -> l);
        subscribePrint(filterableMapped, "filterableMapped");

        Observable<Object> data = Observable.just(Arrays.asList(2, 4)).flatMapIterable(r -> r);
        subscribePrint(data, "d");
    }

    public static void main17(String[] args) {
        // overload of flatMap method
        // This overload is useful when all of the derivative items need to have access to their source item
        // and usually saves us from using some kind of tuple or pair classes,
//        Observable<Integer> flatMapped = Observable.just(5, 8).flatMap(v -> Observable.range(v, 3),
//                (x, y) -> x + y);
//        subcribePrint(flatMapped, "flatMap");

        //overload use to prepend name of file developed in main15
        Observable<String> sd = listFolder(Paths.get("src", "main", "resources"), "{application.properties,lorem_big.txt}")
                .flatMap(path -> from(path),
                        (path, line) -> path.getFileName() + " : " + line);
        subscribePrint(sd, "FileName");
    }

    public static void main16(String[] args) {
        // overload of flatMap method Because of the error, 1 won't be emitted
        Observable<Integer> flatMapped = Observable.just(-1, 0, 1).map(v -> 2 / v).
                flatMap(v -> Observable.just(v),
                        e -> Observable.just(10),
                        () -> Observable.just(42));
        subscribePrint(flatMapped, "flatMap");
    }

    public static void main15(String[] args) {
        // map example
//        Observable<String> mapped = Observable.just(2, 3, 5, 6).map(v -> v * 3).map(v -> (v % 2 == 0 ? "Even" : "Odd"));
//        subcribePrint(mapped, "map");
        // read files from folders using the give patterns
        Observable<String> fsObs = listFolder(Paths.get("src", "main", "resources"), "{lorem_big.txt,application.properties}").
                flatMap(path -> from(path));
        subscribePrint(fsObs, "FS");
    }

    static Observable<Path> listFolder(Path dir, String glob) {
        return Observable.<Path>create(subscriber -> {
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(dir, glob);
                subscriber.add(Subscriptions.create(() -> {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        System.out.println("Error..." + e.getMessage());
                    }
                }));
                Observable.<Path>from(stream).subscribe(subscriber);
            } catch (Exception e) {
                subscriber.onError(e);
                System.out.println("Error..." + e.getMessage());
            }
        });
    }

    static Observable<String> from(final Path path) {
        return Observable.<String>create(subscriber -> {
            try {
                BufferedReader reader = Files.newBufferedReader(path);
                subscriber.add(Subscriptions.create(() -> {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        System.out.println("Error..." + e.getMessage());
                    }
                }));
                String line = null;
                while ((line = reader.readLine()) != null && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(line);
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
                System.out.println("Error..." + e.getMessage());
            }
        });
    }

    public static void main14(String[] args) {
        ///When a or b changes, c is automatically updated to their sum.
        BehaviorSubject<Double> a = BehaviorSubject.create(0.0);
        BehaviorSubject<Double> b = BehaviorSubject.create(0.0);
        BehaviorSubject<Double> c = BehaviorSubject.create(0.0);

        Observable.combineLatest(a, b, (x, y) -> x + y).subscribe(c);
        subscribePrint(c.asObservable(), "Sum");

        a.onNext(5d);
        b.onNext(10d);
        b.onNext(15d);
        a.onNext(1d);
    }

    public static void main13(String[] args) throws Exception {
        // The Subject instances example (hot and also adhoc, onDemand close)
        Observable<Long> interval = Observable.interval(100L, TimeUnit.MILLISECONDS);
        Subject<Long, Long> publishSubject = PublishSubject.create();
        interval.subscribe(publishSubject);

        Subscription sub1 = subscribePrint(publishSubject, "First");
        Subscription sub2 = subscribePrint(publishSubject, "Second");
        Thread.sleep(300L);
        publishSubject.onNext(555L);

        Subscription sub3 = subscribePrint(publishSubject, "Third");
        Thread.sleep(500L);

        sub1.unsubscribe();
        sub2.unsubscribe();
        sub3.unsubscribe();
    }

    public static void main12(String[] args) throws Exception {
        // It can be activated on the first subscription to it and deactivated when every Subscriber instance unsubscribes.
        // This makes an Observable instance to become hot without calling the connect() method.
        Observable<Long> interval = Observable.interval(100L, TimeUnit.MILLISECONDS);
        Observable<Long> refCount = interval.publish().refCount();
        Subscription sub1 = subscribePrint(refCount, "First");
        Thread.sleep(300L);
        Subscription sub2 = subscribePrint(refCount, "Second");
        Thread.sleep(300L);
        sub1.unsubscribe();
        sub2.unsubscribe();
        Subscription sub3 = subscribePrint(refCount, "Third");
        Thread.sleep(300L);
        sub3.unsubscribe();
    }

    public static void main11(String[] args) throws Exception {
        // What if we want to receive all the notifications that have been emitted before
        // our subscription and then to continue receiving the incoming ones? That can be accomplished by calling the replay() method instead of the publish() method
        Observable<Long> interval = Observable.interval(100L, TimeUnit.MILLISECONDS);
        ConnectableObservable<Long> published = interval.replay();
        Subscription sub1 = subscribePrint(published, "First");
        Subscription sub2 = subscribePrint(published, "Second");
        System.out.println("waiting 3 seconds");
        published.connect();
        Thread.sleep(500);
        Subscription sub3 = subscribePrint(published, "Third");
        Thread.sleep(500);
        sub1.unsubscribe();
        sub2.unsubscribe();
        sub3.unsubscribe();
    }

    public static void main10(String[] args) throws Exception {
        // once for each Subscriber. The third Subscriber will join the other two, printing the numbers emitted after the first 500 milliseconds,
        // but it won't print the numbers emitted before its subscription.
        Observable<Long> interval = Observable.interval(100L, TimeUnit.MILLISECONDS);
        ConnectableObservable<Long> published = interval.publish();
        Subscription sub1 = subscribePrint(published, "First");
        Subscription sub2 = subscribePrint(published, "Second");
        System.out.println("waiting 3 seconds");
        published.connect();
        Thread.sleep(500);
        Subscription sub3 = subscribePrint(published, "Third");
        Thread.sleep(500);
        sub1.unsubscribe();
        sub2.unsubscribe();
        sub3.unsubscribe();
    }

    public static void main9(String[] args) throws Exception {
        // unsubscribe from main thread example
        Path path = Paths.get("src", "main", "resources", "lorem_big.txt");
        List<String> data = Files.readAllLines(path);
        Observable<String> observable = fromIterable(data).subscribeOn(Schedulers.computation());
        Subscription subscription = subscribePrint(observable, "File");

        System.out.println("Before unsubscribe!");
        System.out.println("-------------------");
        Thread.sleep(5000);
        subscription.unsubscribe();
        System.out.println("-------------------");
        System.out.println("After unsubscribe");

    }

    static <T> Observable<T> fromIterable(final Iterable<T> iterable) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    Iterator<T> iterator = iterable.iterator();
                    while (iterator.hasNext()) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        subscriber.onNext(iterator.next());
                        Thread.sleep(1000);
                    }
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    public static void main8(String[] args) throws Exception {
        // different kind of observables
        subscribePrint(Observable.interval(500L, TimeUnit.MILLISECONDS), "Interval Observable");

        subscribePrint(Observable.timer(0L, 2L, TimeUnit.SECONDS), "Timed Interval Observable");

        subscribePrint(Observable.timer(2L, TimeUnit.SECONDS), "Timer Observable");

        subscribePrint(Observable.error(new Exception("Test Error!")), "Error Observable");

        subscribePrint(Observable.empty(), "Empty Observable");

        subscribePrint(Observable.never(), "Never Observable");

        subscribePrint(Observable.range(1, 3), "Range Observable");

        Thread.sleep(3000L);
    }

    public static <T> Subscription subscribePrint(Observable<T> observable, String name) {
        return observable.subscribe((v) -> System.out.println(name + " :" + v),
                (e) -> {
                    System.err.println("Error from " + name + ":");
                    System.err.println(e.getMessage());
                }, () -> System.out.println(name + " ended!"));
    }

    public static void main7(String[] args) {
        //Observable.from (List)
        List<String> list = Arrays.asList("blue", "red", "green", "yellow", "orange", "cyna", "purple");
        Observable<String> listObservable = Observable.from(list);

        listObservable.subscribe(System.out::println);
        listObservable.subscribe(r ->
                System.out.print(r + "|"), System.out::println, System.out::println);

        //Observable.from (Folder)
        Path resources = Paths.get("src", "main", "resources");
        try (DirectoryStream<Path> dStream = Files.newDirectoryStream(resources)) {
            Observable<Path> dirObservable = Observable.from(dStream);
            dirObservable.subscribe(System.out::println);
            // below will fail because iterator() method of the DirectoryStream parameter can be called only once.
            // dirObservable.subscribe(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Observable.from (Array)
        Integer[] numbers = {1, 2, 3, 4, 5};
        Observable<Integer> numObservables = Observable.from(numbers);
        numObservables.subscribe(n -> System.out.println(n), (r) -> {
        }, () -> System.out.println("Completed..."));

        //Observable.just (single value/object)
        Observable.just("10").subscribe(r -> System.out.println(r));
        Observable.just("10", "20").subscribe(r -> System.out.println(r), (e) -> {
        }, () -> System.out.println("Done"));

        Observable.just(new User("La", "Mit")).map(r -> r.getFirstname() + " " + r.getLastname()).subscribe(System.out::println);
    }

    private static class User {
        private final String firstname;
        private final String lastname;

        public User(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }
    }

    public static void main6(String[] args) {
        // Functions like these can be used to implement different behaviors that have something in common. In object-oriented programming we define classes and
        // then extend them, overloading their methods. In functional programming, we define higher order functions as interfaces and call them with different parameters, resulting in different behaviors.
        // function that return Function
        System.out.println(greet("Hello").apply("world"));

        System.out.println(greet("Goodbye").apply("cruel world"));

        Function<String, String> howdy = greet("Howdy");

        System.out.println(howdy.apply("Tanya"));
        System.out.println(howdy.apply("Dali"));
    }

    public static Function<String, String> greet(String greeting) {
        return (String name) -> greeting + " " + name + "!";
    }

    public static void main5(String[] args) {
        // higher order function
        System.out.println(highSum(v -> v * v, v -> v + v, 10, 20));

        // higher order functions is to be flexible
        Function<String, Integer> strToInt = s -> Integer.parseInt(s);
        System.out.println(highSum(strToInt, strToInt, "4", "5"));
    }

    public static <T, R> int highSum(Function<T, Integer> f1, Function<R, Integer> f2, T data1, R data2) {
        return f1.apply(data1) + f2.apply(data2);
    }

    public static void main4(String[] args) {
        // passing lambda to method Consumer
        Action<Integer> action = (s) -> System.out.println(" Value is ->" + s);
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(20);
        act(numbers, action);
    }

    public static void act(List<Integer> numbers, Action<Integer> mapped) {
        for (Integer v : numbers) {
            mapped.act(v);
        }
    }

    interface Action<V> {
        void act(V value);
    }

    public static void main3(String[] args) {
        // passing lambda to method Function
        List<Integer> numbers = new ArrayList<>();
        List<Integer> mapped = map(numbers, value -> value * value);

        // giving type in lambda
        Mapper<Integer, Integer> square = (Integer value) -> value * value;
    }

    interface Mapper<V, M> {
        M map(V value);
    }

    public static <V, M> List<M> map(List<V> list, Mapper<V, M> mapper) {
        List<M> mapped = new ArrayList<M>(list.size());
        for (V v : list) {
            mapped.add(mapper.map(v));
        }
        return mapped;
    }

    public static void main2(String[] args) {
        // reactive sum
        AtomicInteger sum = new AtomicInteger(0);
        ConnectableObservable<String> input = from(System.in);

        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);

        Observable.combineLatest(a, b, (z, j) -> z + j).subscribe(next -> {
            sum.set(next.intValue());
            System.out.println("update : a + b = " + sum); // (2)
        }, e -> {
            System.err.println("Got an error!"); // (3)
            e.printStackTrace();

        }, () -> System.out.println("Exiting last sum was :" + sum)); // (6)

        input.connect();
    }

    static ConnectableObservable<String> from(final InputStream stream) {
        return from(new BufferedReader(new InputStreamReader(stream)));
    }

    static ConnectableObservable<String> from(final BufferedReader reader) {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            try {
                String line;
                while (!subscriber.isUnsubscribed() &&
                        (line = reader.readLine()) != null) {
                    if (line == null || line.equalsIgnoreCase("exit")) {
                        break;
                    }
                    subscriber.onNext(line);
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }).publish();
    }

    public static Observable<Double> varStream(final String varName, Observable<String> input) {
        final Pattern pattern = Pattern.compile("^\\s*" + varName
                + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");
        // below code with lamdba
        return input.map(pattern::matcher)
                .filter((matcher) -> matcher.matches() && matcher.group(1) != null)
                .map(matcher -> matcher.group(1))
                .map(Double::parseDouble);
        // below code without lamdba
//        final Pattern pattern = Pattern.compile("^\\s*" + varName
//                + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");
//        return input.map(new Func1<String, Matcher>() {
//            @Override
//            public Matcher call(String str) {
//                return pattern.matcher(str); // (2)
//            }
//        }).filter(new Func1<Matcher, Boolean>() {
//            @Override
//            public Boolean call(Matcher matcher) {
//                return matcher.matches() && matcher.group(1) != null; // (3)
//            }
//        }).map(new Func1<Matcher, Double>() {
//            @Override
//            public Double call(Matcher matcher) {
//                return Double.parseDouble(matcher.group(1)); // (4)
//            }
//        });
    }


    public static void main1(String[] args) {
        // older way
        List<String> list = Arrays.asList("A", "B", "C");
        Iterator<String> listIterator = list.iterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
        System.out.println("\n\n");
        // new way the producer 'pushes' the values as notifications to the consumer.
        List<String> newList = Arrays.asList("One", "Two", "Three", "Four", "Five");
        Observable<String> observable = Observable.from(newList);

        observable.subscribe(r -> {
            System.out.println(r);
        }, e -> {
            System.out.println("Error Occurred...");
        }, () -> System.out.println("Completed..."));

    }
}
