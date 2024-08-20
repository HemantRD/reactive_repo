package com.example.demo.stream.streamchap;

public class Dog implements Comparable<Dog> {
    private String name;
    private int age;
    private int weight;

    public Dog(String name, int age, int weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return this.name + " is " + this.age + " years old and weighs "
                + this.weight + " pounds";
    }

    @Override
    public int compareTo(Dog v1) {
        return this.getName().compareTo(v1.getName());
    }
}
