package com.example.demo.stream.reactive;

import com.example.demo.stream.reactive.book.CreateObservable;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
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

import static com.example.demo.stream.reactive.ReactiveProgChap1To4.subscribePrint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReactiveProgChap7to8 {
    public static void main(String[] args) {
        // creating custom operator
        Observable<Pair<Long, String>> observable = Observable
                .just("a", "b", "c", "d", "e")
                .lift(new Indexed<String>());
        subscribePrint(observable, "Custom Operator");
    }

    public static class Pair<L, R> {
        final L left;
        final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((left == null) ? 0 : left.hashCode());
            result = prime * result + ((right == null) ? 0 : right.hashCode());
            return result;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair other = (Pair) obj;
            if (left == null) {
                if (other.left != null) {
                    return false;
                }
            } else if (!left.equals(other.left)) {
                return false;
            }
            if (right == null) {
                if (other.right != null) {
                    return false;
                }
            } else if (!right.equals(other.right)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    public static class Indexed<T> implements Observable.Operator<Pair<Long, T>, T> {
        private final long initialIndex;

        public Indexed() {
            this(0L);
        }

        public Indexed(long initial) {
            this.initialIndex = initial;
        }

        @Override
        public Subscriber<? super T> call(Subscriber<? super Pair<Long, T>> subscriber) {
            return new Subscriber<T>(subscriber) {
                private long index = initialIndex;

                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onNext(T t) {
                    subscriber.onNext(new Pair<>(index++, t));
                }
            };
        }
    }

    public static void main2(String[] args) {
        //cache the response example not working
        String url = "https://api.github.com/orgs/ReactiveX/repos";
        Observable<ObservableHttpResponse> response = request(url);
        System.out.println("Not yet subscribed.");
        Observable<String> stringResponse = response
                .flatMap(resp -> resp.getContent()
                        .map(bytes -> new String(bytes)))
                .retry(5)
                .cast(String.class)
                .map(String::trim)
                .cache();

        System.out.println("Subscribe 1:");
        System.out.println(stringResponse.toBlocking().first());

        System.out.println("Subscribe 2:");
        System.out.println(stringResponse.toBlocking().first());

    }

    public static void main1(String[] args) {
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

    private Observable<String> tested;
    private List<String> expected;

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