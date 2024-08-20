package com.example.demo.reactive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;

public class ReactiveSum {

    public static void main(String args[]) {
        ConnectableObservable<String> input = from(System.in);
        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);
        //ReactiveSum sum = new ReactiveSum(a, b);
        input.connect();
    }

    static ConnectableObservable<String> from(final InputStream stream) {
//        return from(new BufferedReader(new InputStreamReader(stream)));
        return null;
        // (1)
    }

//	static ConnectableObservable<String> from(final BufferedReader
//			reader) {
//			return Observable.create(new OnSubscribe<String>() { // (2)
//			@Override
//			public void call(Subscriber<? super String> subscriber) {
//			if (subscriber.isUnsubscribed()) { // (3)
//			return;
//			}
//			try {
//			String line;
//			while(!subscriber.isUnsubscribed() &&
//			(line = reader.readLine()) != null) { // (4)
//			if (line == null || line.equals("exit")) { // (5)
//			break;
//			}
//			subscriber.onNext(line); // (6)
//			}
//			}
//			catch (IOException e) { // (7)
//			subscriber.onError(e);
//			}
//			if (!subscriber.isUnsubscribed()) // (8)
//			subscriber.onCompleted();
//			}
//			}
//			}).publish(); // (9)
//
//}}

    public static Observable<Double> varStream(final String varName, Observable<String> input) {
        final Pattern pattern = Pattern.compile("\\^s*" + varName + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$"); // (1)
        return input.map(new Func1<String, Matcher>() {
                    public Matcher call(String str) {
                        return pattern.matcher(str); // (2)
                    }
                }).filter(new Func1<Matcher, Boolean>() {

                    public Boolean call(Matcher matcher) {
                        return matcher.matches() && matcher.group(1) != null; //
                    }
                })
                .map(new Func1<Matcher, Double>() {
                    public Double call(Matcher matcher) {
                        return Double.parseDouble(matcher.group(1)); // (4)

                    }
                });
    }
}
