package com.example.demo.stream.reactive;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReactiveProgramming {

    public static void main(String[] args) {
        // reactive sum
        ConnectableObservable<String> input = from(System.in);

        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);

        ReactiveSum sum = new ReactiveSum(a, b);
        input.connect();
    }

    static ConnectableObservable<String> from(final InputStream stream) {
        return from(new BufferedReader(new InputStreamReader(stream)));
    }

    static ConnectableObservable<String> from(final BufferedReader reader) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
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
            }
        }).publish();
    }

    public static Observable<Double> varStream(final String varName, Observable<String> input) {
        final Pattern pattern = Pattern.compile("^\\s*" + varName
                + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");
        return input.map(new Func1<String, Matcher>() {
            @Override
            public Matcher call(String str) {
                return pattern.matcher(str); // (2)
            }
        }).filter(new Func1<Matcher, Boolean>() {
            @Override
            public Boolean call(Matcher matcher) {
                return matcher.matches() && matcher.group(1) != null; // (3)
            }
        }).map(new Func1<Matcher, Double>() {
            @Override
            public Double call(Matcher matcher) {
                return Double.parseDouble(matcher.group(1)); // (4)
            }
        });
    }

    public static final class ReactiveSum implements Observer<Double> { // (1)
        private double sum;

        public ReactiveSum(Observable<Double> a, Observable<Double> b) {
            this.sum = 0;
            Observable.combineLatest(a, b, new Func2<Double, Double, Double>() { // (5)
                public Double call(Double a, Double b) {
                    return a + b;
                }
            }).subscribe(this); // (6)
        }

        public void onCompleted() {
            System.out.println("Exiting last sum was :" + this.sum); // (4)
        }

        public void onError(Throwable e) {
            System.err.println("Got an error!"); // (3)
            e.printStackTrace();
        }

        public void onNext(Double sum) {
            this.sum = sum;
            System.out.println("update : a + b = " + sum); // (2)
        }
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
