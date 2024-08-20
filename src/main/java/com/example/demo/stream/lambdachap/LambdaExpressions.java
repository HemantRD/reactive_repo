package com.example.demo.stream.lambdachap;

import java.util.*;
import java.util.function.*;

public class LambdaExpressions {
    // TODO implement and read SELF TEST again
    public static void main(String[] args) {
        // custom functional interface TriPredicate
        TriPredicate<String, Integer, Integer> triPredicate = (s, n, w) -> {
            if (s.equals("There is no spoon") && n > 2 && w < n) {
                return true;
            } else {
                return false;
            }
        };
        System.out.println("Pass the test? " +
                triPredicate.test("Follow the White Rabbit", 2, 3));
        System.out.println("Pass the test? " +
                triPredicate.test("There is no spoon", 101, 3));
    }

    public static void main14(String[] args) {
        // UnaryOperator takes double and returns double. not required to mention both types
        UnaryOperator<Double> doubleUnaryOperator = (d) -> {
            return Math.log(d) / Math.log(2);
        };
        System.out.println(doubleUnaryOperator.apply(8d));

        System.out.println("\n\n\n");
        // method reference
        List<String> trees = Arrays.asList("A", "B", "C");
        trees.forEach(System.out::println);

        System.out.println("\n\n\n");
        //calling custom method via method reference
        trees.forEach(LambdaExpressions::print);
    }

    public static void print(String name) {
        System.out.println("name " + name);
    }

    public static void main13(String[] args) {
        // identity method return same value
        Function<Integer, Integer> function = Function.identity();
        System.out.println(function.apply(10));

        //DoubleFunction
        DoubleFunction doubleFunction = (d) -> 10.5;
        System.out.println(doubleFunction.apply(6));

    }

    public static void main12(String[] args) {
        //BiFunction in JDK example
        Map<String, String> aprilWinners = new HashMap<>();
        aprilWinners.put("April 2017", "Bob");
        aprilWinners.put("April 2016", "Annette");
        aprilWinners.put("April 2015", "Lamar");

        System.out.println("--- List before checking April 2014");
        aprilWinners.forEach((k, v) -> System.out.println(k + " : " + v));

        aprilWinners.computeIfAbsent("April 2014", (k) -> "John Doe");

        System.out.println("--- List, after checking April 2014 ---");
        aprilWinners.forEach((k, v) -> System.out.println(k + " : " + v));

        aprilWinners.replaceAll((k, v) -> v.toUpperCase());
        System.out.println("--- List after replace all ---");
        aprilWinners.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    public static void main11(String[] args) {
        // Function interface
        Function<Integer, String> function = (a) -> {
            return String.valueOf(a + " an");
        };
        System.out.println(function.apply(10));

        System.out.println("\n\n\n");
        // BiFunction
        BiFunction<String, String, String> biFunction = (a, b) -> a + " " + b;
        System.out.println(biFunction.apply("Hemant", "Ghaydar"));
    }

    public static void main10(String[] args) {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Your Brain is Better with Java", 58.99));
        books.add(new Book("OCP8 Java Certification Study Guide", 53.39));
        books.add(new Book("Is Java Coffee or Programming?", 39.86));
        books.add(new Book("While you were out Java happened", 12.99));

        // and predicate example
        Predicate<Book> javaBuy = b -> b.getName().contains("Java");
        Predicate<Book> byPrice = b -> b.getPrice() < 55.0;
        Predicate<Book> definitelyBuy = javaBuy.and(byPrice);
        books.forEach(book -> {
            if (definitelyBuy.test(book)) {
                System.out.println("You should definitely buy " + book.getName() + " with price :" + book.getPrice());
            }
        });
    }

    public static void main9(String[] args) {
        // IntPredicate
        IntPredicate answer = i -> i == 42;
        System.out.println(answer.test(42));

        // Predicate.isEqual method
        System.out.println("\n\n\n");
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("boi", 31, 6);
        Predicate<Dog> dogPredicate = Predicate.isEqual(boi);
        System.out.println(dogPredicate.test(clover));
    }

    public static void main8(String[] args) {
        // predicate negate method example
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog zooey = new Dog("zooey", 45, 8);
        List<Dog> dogs = new ArrayList<>();
        dogs.add(boi);
        dogs.add(clover);
        dogs.add(zooey);

        Predicate<Dog> age = r -> r.getAge() == 35;
        dogs.stream().filter(age.negate()).forEach(r -> System.out.println(r.getName()));
        System.out.println(age.negate().test(boi));

        System.out.println("\n\n\n");
        // and() method example
        Predicate<Dog> agePredicate = d -> d.getAge() == 30;
        Predicate<Dog> namePredicate = d -> d.getName().equalsIgnoreCase("boi");
        Predicate<Dog> nameAgePredicate = agePredicate.and(namePredicate);
        dogs.stream().filter(nameAgePredicate).forEach(r -> System.out.println(r.getName()));

    }

    public static void main7(String[] args) {
        // Predicate example
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Predicate<Dog> dogPredicate = (d) -> d.getWeight() > 10;
        System.out.println(dogPredicate.test(boi));
        System.out.println(dogPredicate.test(clover));

        System.out.println("\n\n\n");
        // Predicate pass as argument
        List<Dog> dogs = new ArrayList<>();
        Dog zooey = new Dog("zooey", 45, 8);
        dogs.add(boi);
        dogs.add(clover);
        dogs.add(zooey);
        printDogif(dogs, p -> p.getWeight() > 10);
        printDogif(dogs, p -> p.getWeight() > 5);

        // ArrayList removeIf method
        System.out.println("\n\n\n");
        dogs.removeIf(d -> d.getName().startsWith("c"));
        dogs.forEach(r -> System.out.println(r.getName()));

    }

    public static void printDogif(List<Dog> dogs, Predicate<Dog> dogPredicate) {
        dogs.forEach(d -> {
            if (dogPredicate.test(d)) {
                System.out.println(d);
            }
        });
    }

    public static void main6(String[] args) {
        // can't change variable out of forEach but you can change field of an object
        User user = new User();
        List<String> names = Arrays.asList("A", "B", "C");
        System.out.println(user.getUsername());
        names.forEach(r -> {
            System.out.println(user.getUsername());
            System.out.println(r);
            user.setUsername("sd");
            System.out.println(user.getUsername());
        });
        System.out.println(user.getUsername());

        System.out.println("\n\n\n");
        //andThen method example  default method of the Consumer interface
        //  "composed Consumer" example
        List<Dog> dogs = new ArrayList<>();
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog zooey = new Dog("zooey", 45, 8);
        dogs.add(boi);
        dogs.add(clover);
        dogs.add(zooey);
        Consumer<Dog> displayName = r -> System.out.print(r + " ");
        dogs.forEach(displayName.andThen(r -> r.bark()));

        // below want work
        // dogs.forEach((d -> System.out.print(d + " ")).andThen(d -> d.bark()));
        // below will work
        Consumer<Dog> s = r -> {
            System.out.print(r + " ");
        };
        Consumer<Dog> s1 = r -> r.bark();
        dogs.forEach(s.andThen(s1));
    }

    public static void main5(String[] args) {
        // forEach example
        List<String> names = Arrays.asList("A", "B", "C");
        Consumer<String> print = (s) -> System.out.println(s);
        names.forEach(r -> System.out.println(r));

        System.out.println("\n\n\n");
        // map with BiConsumer example
        Map<String, String> env = System.getenv();
        BiConsumer<String, String> biConsumer = (k, v) -> {
            System.out.println("Key is :" + k + " value is :" + v);
        };
        env.forEach(biConsumer);
    }

    public static void main4(String[] args) {
        // consumer example
        Consumer<String> consumer = (pill) -> {
            if (pill.equalsIgnoreCase("red")) {
                System.out.println("It is red");
            }
            if (pill.equalsIgnoreCase("blue")) {
                System.out.println("It is blue");
            }
        };
        consumer.accept("blue");

        System.out.println("\n\n\n");
        // BiConsumer example
        BiConsumer<String, String> printEnv = (key, value) -> {
            System.out.println(key + " value -> " + value);
        };
        Map<String, String> entry = System.getenv();
        printEnv.accept("USER", entry.get("USER"));
    }

    public static void main3(String[] args) {
        // get system username using Supplier
        Supplier<String> userName = () -> {
            Map<String, String> map = System.getenv();
            return map.get("USER");
        };
        System.out.println(userName.get());

        // get random value using supplier
        Random random = new Random();
        IntSupplier supplier = () -> random.nextInt();
        System.out.println(supplier.getAsInt());
    }

    public static void main2(String[] args) {
        Dog boi = new Dog("boi", 30, 6);
        int numCats = 3;
        DogQuerier dqWithCats = d -> {
            int numBalls = 1;
            numBalls++;
//            numCats; // Won't compile! Can't change numCats
            System.out.println("Number of balls: " + numBalls);
            System.out.println("Number of cats: " + numCats);
            return d.getAge() > 9;
        };
        DogsPlay dp = new DogsPlay(dqWithCats);
        System.out.println("Is Clover older than 9? " + dp.doQuery(boi));
        System.out.println("--- use DogsPlay ---");

        System.out.println("\n\n\n");
        // create supplier that returns value
        Supplier<Integer> val = () -> 10;
        System.out.println(val.get());

    }

    public static void main1(String[] args) {
        Dog boi = new Dog("boi", 30, 6);
        DogQuerier dq = d -> d.getAge() > 10;
        DogQuerier dq4 = (d) -> d.getAge() > 10;
        DogQuerier dq1 = (Dog d) -> d.getAge() > 10;
        DogQuerier dq3 = (Dog d) -> {
            return d.getAge() > 10;
        };
        System.out.println(dq3.test(boi));
    }

    interface DogQuerier {
        public boolean test(Dog d);
    }

    static class DogsPlay {
        DogQuerier dogQuerier;

        public DogsPlay(DogQuerier dogQuerier) {
            this.dogQuerier = dogQuerier;
        }

        public boolean doQuery(Dog d) {
            return dogQuerier.test(d);
        }
    }
}

