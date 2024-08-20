package com.example.demo.stream.streamchap;

public class Sensor {
    String value = "up";
    int i = 0;

    public Sensor() {

    }

    public String next() {
        i = i + 1;
        return i > 10 ? "down" : "up";
    }
}
