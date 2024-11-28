package com.example.demo.stream.reactive;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
    public void testAsynchronousInBlockingWay() {
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