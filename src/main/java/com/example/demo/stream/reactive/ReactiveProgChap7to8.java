package com.example.demo.stream.reactive;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReactiveProgChap7to8 {

    private Observable<String> tested;
    private List<String> expected;

    @Before
    public void before() {
        tested = sorted((a, b) -> a.compareTo(b), "Star", "Bar", "Car", "War", "Far", "Jar");
        expected = Arrays.asList("Bar", "Car", "Far", "Jar", "Star", "War");
    }

    @Test
    public void testUsingTestSubscriber() {
        //testUsingTestSubscriber
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        tested.subscribe(subscriber);
        Assert.assertEquals(expected, subscriber.getOnNextEvents());
        Assert.assertSame(1, subscriber.getOnCompletedEvents().size());
        Assert.assertTrue(subscriber.getOnErrorEvents().isEmpty());
        Assert.assertTrue(subscriber.isUnsubscribed());
        // another way of test
        subscriber.assertReceivedOnNext(expected);
        subscriber.assertTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertUnsubscribed();
    }

    @Test
    public void testUsingNormalSubscription() {
        TestData data = new TestData();
        tested.subscribe((v) -> data.getResult().add(v),
                (e) -> data.setError(e),
                () -> data.setCompleted(true));

        Assert.assertTrue(data.isCompleted());
        Assert.assertNull(data.getError());
        Assert.assertEquals(expected, data.getResult());
    }

    @Test
    public void testUsingBlockingObservable() {
        List<String> result = tested.toList().toBlocking().single();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferentMethods() {
        // count method example
        Observable
                .range(10, 100).
                count()
                .subscribe(System.out::println);

        //toList method example
        Observable
                .range(5, 15)
                .toList()
                .subscribe(System.out::println);

        //toList with toBlocking method example
        List<Integer> single = Observable
                .range(5, 15)
                .toList()
                .toBlocking().single();
        System.out.println(single);
    }

    @Test
    public void testAsynchronousInBlockingWay() {
        //testAsynchronousInBlockingWay, interval is asynchronous
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(5)
                .toBlocking()
                .forEach(System.out::println);
        System.out.println("END");

        Integer first = Observable.range(3, 13).toBlocking().first();
        System.out.println(first);

        Integer last = Observable.range(3, 13).toBlocking().last();
        System.out.println(last);
    }

    @Test
    public void testNextMethod() throws Exception {
        // test next() method
        Iterable<Long> next = Observable.interval(100L, TimeUnit.MILLISECONDS).toBlocking().next();
        Iterator<Long> iterator = next.iterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());

        // test latest() method
        Iterable<Long> latest = Observable.interval(1000L, TimeUnit.MILLISECONDS).toBlocking().latest();
        iterator = latest.iterator();
        System.out.println(iterator.next());
        Thread.sleep(5500l);
        System.out.println(iterator.next());
        System.out.println(iterator.next());
    }

    @SafeVarargs
    public static <T> Observable<T> sorted(Comparator<? super T> comparator, T... data) {
        List<T> listData = Arrays.asList(data);
        listData.sort(comparator);

        return Observable.from(listData);
    }

    private class TestData {
        private Throwable error = null;
        private boolean completed = false;
        private List<String> result = new ArrayList<>();

        public void setError(Throwable error) {
            this.error = error;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public List<String> getResult() {
            return result;
        }

        public Throwable getError() {
            return error;
        }


    }


}