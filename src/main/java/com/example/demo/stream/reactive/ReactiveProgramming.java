package com.example.demo.stream.reactive;

import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ReactiveProgramming {

    public static void main(String[] args) {
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
