package com.example.demo.stream.lambdachap;

public class Dog {
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

    public void bark() {
        System.out.println("Woof!");
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name.length() * 5 + (age * 5) + (weight * 5);
    }

    @Override
    public boolean equals(Object obj) {
        Dog dog = (Dog) obj;
        if (dog.name.equalsIgnoreCase(this.getName())
                && dog.getAge() == this.getAge() && dog.getWeight() == dog.getWeight()) {
            return true;
        } else {
            return false;
        }

    }
}
