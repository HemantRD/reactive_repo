package com.example.demo.stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stream1 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.forEach(System.out::println);
    }

    public static void main1(String[] args) {
        // find duplicate number list
        Stream<Integer> integerStream = Stream.of(2, 17, 5,
                20, 17, 30,
                4, 23, 59, 23);
        Set<Integer> hashSet = new HashSet<>();
        List<Integer> dataList = integerStream.filter(r -> !hashSet.add(r)).collect(Collectors.toCollection(() -> new ArrayList<>()));
        System.out.println(dataList);
    }


}

