package com.example.demo.stream.reactive;

import com.example.demo.stream.reactive.book.CreateObservable;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReactiveProgChap7to8 {

    private Observable<String> tested;
    private List<String> expected;

    public static void main(String[] args) {
        // close resources automatically with using (i.e close CloseableHttpAsyncClient) not working
        String url = "https://api.github.com/orgs/ReactiveX/repos";
        Observable<ObservableHttpResponse> response = request(url);

        System.out.println("Not yet subscribed.");

        Observable<String> stringResponse = response.<String>flatMap(resp -> resp.getContent()
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .retry(5)
                .map(String::trim));

        System.out.println("Subscribe 1:");
        System.out.println(stringResponse.toBlocking().first());

        System.out.println("Subscribe 2:");
        System.out.println(stringResponse.toBlocking().first());
    }

    public static Observable<ObservableHttpResponse> request(String url) {
        Func0<CloseableHttpAsyncClient> resourceFactory = () -> {
            CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
            client.start();
            System.out.println(Thread.currentThread().getName() + " : Created and started the client.");
            return client;
        };
        Func1<HttpAsyncClient, Observable<ObservableHttpResponse>> observableFactory = (client) -> {
            System.out.println(Thread.currentThread().getName() + " : About to create Observable.");
            return ObservableHttp.createGet(url, client).toObservable();
        };
        Action1<CloseableHttpAsyncClient> disposeAction = (client) -> {
            try {
                System.out.println(Thread.currentThread().getName() + " : Closing the client");
                client.close();
            } catch (Exception e) {
            }
        };
        return Observable.using(resourceFactory, observableFactory, disposeAction);
    }

    @Before
    public void before() {
        tested = sorted((a, b) -> a.compareTo(b), "Star", "Bar", "Car", "War", "Far", "Jar");
        expected = Arrays.asList("Bar", "Car", "Far", "Jar", "Star", "War");
    }

    @Test
    public void testBehaveAsNormalIntervalWithOneGap() {
        TestScheduler testScheduler = Schedulers.test();
        Observable<Long> interval = CreateObservable.interval(Arrays.asList(100L), TimeUnit.MILLISECONDS, testScheduler);
        TestSubscriber<Long> subscriber = new TestSubscriber<>();
        interval.subscribe(subscriber);
        assertTrue(subscriber.getOnNextEvents().isEmpty());
        testScheduler.advanceTimeBy(101L, TimeUnit.MILLISECONDS);
        assertEquals(Arrays.asList(0l), subscriber.getOnNextEvents());
        testScheduler.advanceTimeBy(101L, TimeUnit.MILLISECONDS);
        assertEquals(Arrays.asList(0l, 1l),
                subscriber.getOnNextEvents());
        testScheduler.advanceTimeTo(1L, TimeUnit.SECONDS);
        assertEquals(Arrays.asList(0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l), subscriber.getOnNextEvents());
    }

    @Test
    public void testUsingTestSubscriber() {
        //testUsingTestSubscriber
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        tested.subscribe(subscriber);
        Assert.assertEquals(expected, subscriber.getOnNextEvents());
        Assert.assertSame(1, subscriber.getOnCompletedEvents().size());
        assertTrue(subscriber.getOnErrorEvents().isEmpty());
        assertTrue(subscriber.isUnsubscribed());
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

        assertTrue(data.isCompleted());
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