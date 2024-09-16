package com.example.demo.stream.streamchap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamChapter {

    // TODO implement and read SELF TEST again
    public static void main(String[] args) {
        // taste of parallelStream
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        int sum = list.stream().mapToInt(r -> r).sum();
        System.out.println(sum);

        System.out.println(list.parallelStream().mapToInt(r -> r).sum());

        // not in the order of the source
        System.out.println("\n\n\n");
        list.stream().parallel().forEach(System.out::println);
    }

    public static void main25(String[] args) {
        // infinite stream
        // Stream.iterate(0, s -> s + 1).limit(10).forEach(System.out::println);
        Sensor s = new Sensor();
        Stream<String> sensorStream = Stream.generate(() -> s.next());
        sensorStream.filter(r -> r.equalsIgnoreCase("down")).findFirst().ifPresent(r -> System.out.println(r));

        // range and rangeClosed
        IntStream stream = IntStream.rangeClosed(1, 5);
        stream.forEach(r -> System.out.println(r));

        System.out.println("\n\n\n");
        IntStream.rangeClosed(1, 20).filter(r -> r % 2 == 0).limit(5).forEach(System.out::println);

        //skip
        System.out.println("\n\n\n");
        IntStream.rangeClosed(1, 20).filter(r -> r % 2 == 0).skip(5).forEach(System.out::println);
    }

    public static void main24(String[] args) throws Exception {
        // stream of stream and flatMap. Read Line number 649
        Stream<String> stringStream = Files.lines(Paths.get("/Users/hemantkumar.ghaydar/Documents/projects/alloy-recon copy/recon-service/src/main/java/com/apple/ist/alloy/stream/java.txt"));
        Stream<String[]> stream = stringStream.map(r -> r.split(" "));
//        System.out.println(stringStream);
//        System.out.println(stream);
//        stream.map(array -> Arrays.stream(array)).forEach(System.out::println);
        Stream<Stream<String>> ss =
                stream.map(array -> Arrays.stream(array));
//        ss.flatMap(r -> (r)).forEach(r -> System.out.println(r));

        // final code
        long count = Files.lines(Paths.get("/Users/hemantkumar.ghaydar/Documents/projects/alloy-recon copy/recon-service/src/main/java/com/apple/ist/alloy/stream/java.txt")
        ).map(r -> r.split(" ")).flatMap(r -> Arrays.stream(r)).filter(r -> r.equalsIgnoreCase("java")).count();
        System.out.println(count);
    }

    public static void main23(String[] args) {
        // joining, maxBy and minBy
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("clover", 50, 10);
        Dog zooey = new Dog("berth", 45, 8);
        Dog charis = new Dog("berth", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        String groups = dogs.stream().filter(r -> r.getName().startsWith("b")).map(Dog::getName).collect(Collectors.joining(","));
        System.out.println(groups);
        Optional<Dog> optionalDog = dogs.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dog::getAge)));
        optionalDog.ifPresent(r -> System.out.println(r.getAge()));

        optionalDog = dogs.stream().collect(Collectors.minBy(Comparator.comparingInt(Dog::getAge)));
        optionalDog.ifPresent(r -> System.out.println(r.getAge()));

    }

    public static void main22(String[] args) {
        // summingInt and averagingInt
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("clover", 50, 10);
        Dog zooey = new Dog("berth", 45, 8);
        Dog charis = new Dog("berth", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        Map<String, Integer> groups = dogs.stream().filter(d -> d.getName().startsWith("b"))
                .collect(Collectors.groupingBy(Dog::getName, Collectors.summingInt(Dog::getAge)));
        System.out.println(groups);
        System.out.println("\n\n\n\n");

        Map<String, Double> groups1 = dogs.stream().collect(Collectors.groupingBy(Dog::getName, Collectors.averagingInt(Dog::getAge)));
        System.out.println(groups1);
    }

    public static void main21(String[] args) {
        // partitioningBy method
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi more", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        Map<Boolean, List<Dog>> groups = dogs.stream().collect(Collectors.partitioningBy(r -> r.getAge() > 40));
        System.out.println(groups);

        System.out.println("\n\n\n");
        Map<Boolean, List<Integer>> groupsStr = dogs.stream()
                .collect(Collectors.partitioningBy(r -> r.getAge() > 40, Collectors.mapping(r -> r.getAge(), Collectors.toList())));
        System.out.println(groupsStr);
    }

    public static void main20(String[] args) {
        // groupingBy method
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi more", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        Map<Integer, List<Dog>> groupByAge = dogs.stream().collect(Collectors.groupingBy(Dog::getAge));
        System.out.println(groupByAge);

        Map<Integer, Long> groupByCount = dogs.stream().collect(Collectors.groupingBy(Dog::getAge, Collectors.counting()));
        System.out.println(groupByCount);

        System.out.println("\n\n\n");
        // group by count get names only
        Map<Integer, List<String>> groups = dogs.stream()
                .collect(Collectors.groupingBy(Dog::getAge, Collectors.mapping(Dog::getName, Collectors.toList())));
        System.out.println(groups);
    }

    public static void main19(String[] args) throws Exception {
        // another way to read files in one go using streams.
        String filePath = "/Users/hemantkumar.ghaydar/Documents/projects/alloy-recon copy/recon-service/src/main/java/com/apple/ist/alloy/stream/dvdinfo.txt";
        Stream<String> fileStreamList = Files.lines(Path.of(filePath));
        List<String> list = fileStreamList.collect(Collectors.toList());
        list.stream().forEach(System.out::println);
    }

    public static void main18(String[] args) {
        // collect and specific array list example
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi more", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        List<Dog> greater30Dogs = dogs.stream().filter(r -> r.getAge() > 30).collect(Collectors.toList());
        greater30Dogs.stream().sorted(Comparator.comparingInt(Dog::getAge)).forEach(System.out::println);
        System.out.println("\n\n\n");
        List<Dog> greater40Dogs = dogs.stream().filter(r -> r.getAge() > 40).collect(Collectors.toCollection(ArrayList::new));
        greater40Dogs.stream().forEach(System.out::println);
        // another way
        System.out.println("\n\n\n");
        greater40Dogs = dogs.stream().filter(r -> r.getAge() > 40).collect(Collectors.toCollection(() -> new ArrayList<>()));
    }

    public static void main17(String[] args) {
        // error-prone code, do not modify the source while streaming, instead collect it using collect method.
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi more", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        dogs.stream().filter(d -> {
            if (d.getWeight() < 50) {
                dogs.remove(d);
                return false;
            }
            return true;
        }).forEach(System.out::println);
    }

    public static void main16(String[] args) {
        // sorted method
//        List<String> stringList = Arrays.asList("ABC", "XYZ", "CCC");
//        stringList.stream().sorted().forEach(e -> System.out.println(e));
        // sorted method
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog boi1 = new Dog("boi more", 35, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(boi1);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        //dogs.stream().sorted().forEach(r -> System.out.println(r.getName()));
        // comparator
        //dogs.stream().sorted((v1, v2) -> v1.getAge() < v2.getAge() ? 1 : 0).forEach(r -> System.out.println(r.getAge()));
        // another way to use comparator
/*        Comparator<Dog> comparator = (v1, v2) -> v1.getWeight() - v2.getWeight();
        // keep eye on - for sort
        dogs.stream().sorted(comparator).forEach(r -> System.out.println(r.getWeight()));*/

        // static comparator method
        /*Comparator<Dog> byName = (v1, v2) -> v1.getName().compareTo(v2.getName());
        Comparator<Dog> byAge = (v1, v2) -> v1.getAge() - v2.getAge();
        Comparator<Dog> byWeight = (v1, v2) -> v1.getWeight() - v2.getWeight();

        dogs.stream().sorted(byName).forEach(r -> System.out.println(r.getName()));
        dogs.stream().sorted(byAge).forEach(r -> System.out.println(r.getAge()));
        dogs.stream().sorted(byWeight).forEach(r -> System.out.println(r.getWeight()));

        dogs.stream().sorted(byWeight.reversed()).forEach(r -> System.out.println(r.getWeight()));

        //thenComparing
        System.out.println("===========");
        dogs.stream().sorted(byName.thenComparing(byAge)).forEach(System.out::println);*/

        // distinct method
        dogs.stream().map(r -> r.getWeight()).distinct().forEach(System.out::println);
    }

    public static void main15(String[] args) {
        // findAny method
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        Optional<Dog> optionalDog = dogs.stream().peek(e -> System.out.println(e)).filter(r -> r.getAge() > 45).filter(r -> r.getWeight() > 2).findAny();
//        optionalDog.ifPresent(r -> System.out.println(r));
    }

    public static void main14(String[] args) {
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog aiko = new Dog("aiko", 50, 10);
        Dog zooey = new Dog("zooey", 45, 8);
        Dog charis = new Dog("charis", 120, 7);
        dogs.add(boi);
        dogs.add(clover);
        dogs.add(aiko);
        dogs.add(zooey);
        dogs.add(charis);
        // anyMatch method
        boolean result = dogs.stream().filter(r -> r.getAge() > 50).anyMatch(r -> r.getName().startsWith("c"));
        System.out.println(" > 50 and starts with c exist ?  " + result);
        // allMatch
        result = dogs.stream().map(r -> r.getAge()).allMatch(r -> r > 5);
        System.out.println("all dogs > 5 ? " + result);
        //noneMatch
        result = dogs.stream().map(r -> r.getName()).noneMatch(r -> r.equals("red"));
        System.out.println("nobody is red ? " + result);
    }

    public static void main13(String[] args) {
//        DvdInfo dvdInfo = null;
//        Optional<DvdInfo> dvdInfo1 = Optional.of(dvdInfo);
//        dvdInfo1.ifPresent(r -> System.out.println(r));

        // ifPresent method
//        DvdInfo dvdInfo2 = null;
//        Optional<DvdInfo> dvdInfo3 = Optional.ofNullable(dvdInfo2);
//        dvdInfo3.ifPresent(r -> System.out.println(r));
//        if (dvdInfo3.isEmpty()) {
//            System.out.println("Empty found");
//        }
        // empty method
//        Stream<Double> doubleStream = Stream.of(10.2, 10.2, 10.3);
//        Optional<Double> optionalDouble = Optional.empty();
//        optionalDouble = doubleStream.findFirst();
//        optionalDouble.ifPresent(r -> System.out.println(r));
        // orElse method
        Stream<Double> doubleStream = Stream.of(10.2, 10.3, 10.4);
        Optional<Double> optionalDouble = doubleStream.filter(e -> e > 10).findFirst();
        System.out.println(optionalDouble.orElse(10d));
    }

    public static void main12(String[] args) {
        // Double object stream
        Stream<Double> doubleStream = Stream.of(10.2, 10.2, 10.3);
        Optional<Double> optionalDouble = doubleStream.findFirst();
        if (optionalDouble.isPresent()) {
            System.out.println("value present is :" + optionalDouble.get());
        } else {
            System.out.println("optional is empty");
        }
        // another method ifPresent
        Stream.of(10.2, 10.2, 10.3).findFirst().ifPresent(r -> System.out.println(r));

    }

    public static void main11(String[] args) {
        // Average is -> 406.2125
        List<Reading> readings = Arrays.asList(
                new Reading(2017, 1, 1, 405.91),
                new Reading(2017, 1, 8, 405.98),
                new Reading(2017, 1, 15, 406.14),
                new Reading(2017, 1, 22, 406.48),
                new Reading(2017, 1, 29, 406.20),
                new Reading(2017, 2, 5, 407.12),
                new Reading(2017, 2, 12, 406.03));

        // find ave using reduction, will not work since average is not associative
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).filter(v -> v >= 406.0 && v < 407.00).reduce((v1, v2) -> (v1 + v2) / 2));
        // find min using reduction
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).reduce((v1, v2) -> v1 <= v2 ? v1 : v2));
        // find max using reduction
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).reduce((x1, x2) -> x1 >= x2 ? x1 : x2));

    }

    public static void main10(String[] args) {
        // reduce example To write our own method to sum all the values in the stream using reduce(),
        List<Reading> readings = Arrays.asList(
                new Reading(2017, 1, 1, 405.91),
                new Reading(2017, 1, 8, 405.98),
                new Reading(2017, 1, 15, 406.14),
                new Reading(2017, 1, 22, 406.48),
                new Reading(2017, 1, 29, 406.20),
                new Reading(2017, 2, 5, 407.12),
                new Reading(2017, 2, 12, 406.03));
        System.out.println(readings.stream().map(t -> t.getValue()).reduce((r1, r2) -> r1 - r2));

        // There min and max method also present in this
        // available only in primitive DoubleStream not in the Stream<Double>
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).min());
        System.out.println(readings.stream().map(r -> r.getValue()).mapToDouble(r -> r).max());
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).sum());

        // adding initial value in reduce
        System.out.println(readings.stream().mapToDouble(r -> r.getValue()).reduce(500, (p1, p2) -> p1 + p2));
    }

    public static void main9(String[] args) {

        List<Reading> readings = Arrays.asList(
                new Reading(2017, 1, 1, 405.91),
                new Reading(2017, 1, 8, 405.98),
                new Reading(2017, 1, 15, 406.14),
                new Reading(2017, 1, 22, 406.48),
                new Reading(2017, 1, 29, 406.20),
                new Reading(2017, 2, 5, 407.12),
                new Reading(2017, 2, 12, 406.03));

        DoubleStream.builder().build();

        OptionalDouble optionalDouble = readings.stream().mapToDouble(r -> r.getValue()).filter(r -> r >= 406 && r <= 407).average();
        if (optionalDouble.isPresent()) {
            System.out.println("Average is -> " + optionalDouble.getAsDouble());
        } else {
            System.out.println("Optional is empty");
        }

    }

    public static void main8(String args[]) {
        // Map method example
        List<Integer> listArr = Arrays.asList(1, 2, 3, 4, 5, 6);
//        long result = listArr.stream()
//                .peek(n -> System.out.print("Number is: " + n + ", ")) // print the number
//                .map(n -> n * n)
//                .filter(n -> n > 20)
//                .peek(n -> System.out.print("Square is: " + n + ", ")) // print the square
//                .count();

        long result1 = listArr.stream().peek(n -> System.out.print("b "))
                .map(r -> r * r).filter(n -> n > 20).count();
    }

    public static void main7(String args[]) {
        //forEach method
        String[] dataArray = {"ABC", "PQR", "XYZ"};
        Arrays.stream(dataArray).filter(r -> r.startsWith("A") || r.startsWith("P")).filter(r -> r.length() > 2).forEach(System.out::println);
    }

    public static void main6(String args[]) throws Exception {
        // read file and collect to list of object using stream
        String filePath = "/Users/hemantkumar.ghaydar/Documents/projects/alloy-recon copy/recon-service/src/main/java/com/apple/ist/alloy/stream/dvdinfo.txt";
        List<DvdInfo> dvdList = new ArrayList<>();
        try (Stream<String> files = Files.lines(Paths.get(filePath))) {
            files.forEach(line -> {
                String dvd[] = line.split("/");
                DvdInfo dvdInfo = new DvdInfo(dvd[0], dvd[1], dvd[2]);
                dvdList.add(dvdInfo);
            });
        }
        System.out.println(dvdList.size());
    }

    public static void main5(String[] args) {
        // count in different ways
        String[] arr = {"ABC", "XYZ", "PQR"};
        System.out.println(Stream.of(arr).count());
        System.out.println(Arrays.stream(arr).count());
    }

    public static void main4(String[] args) {
        // Stream.of method
        Integer[] arr = {1, 2, 3, 4};
        Stream<Integer> integerStream = Stream.of(arr);
        System.out.println(integerStream.filter(r -> r > 2).count());
    }

    public static void main3(String[] args) {
        // Stream on Map
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 10);
        map.put("B", 15);
        map.put("C", 11);
        map.put("D", 18);
        map.entrySet();
        System.out.println(map.entrySet().stream().filter(r -> r.getValue() > 12).count());
    }

    public static void main2(String[] args) {
        List<Double> doubleList = Arrays.asList(10.5, 11.5, 12.5, 13.5, 14.5);
        Stream<Double> doubleStream = doubleList.stream();
        System.out.println(doubleStream.filter(r -> r == 10).count());
        System.out.println(doubleList.stream().filter(r -> r > 12).count());
    }

    public static void main1(String[] args) {
        Integer arr[] = {1, 2, 3};
        Stream<Integer> integerStream = Arrays.stream(arr);
        System.out.println(integerStream.filter(t -> t > 1).count());
        integerStream = Arrays.stream(arr);
        System.out.println(integerStream.filter(t -> t > 2).count());
    }
}
