package com.example.demo.stream.threadchap;

import java.util.HashSet;
import java.util.Set;

public class Show {
    private static volatile Show INSTANCE;
    private Set<String> availableSeats;

    public static synchronized Show getInstance() { // create a singleton instance
        if (INSTANCE == null) {
            INSTANCE = new Show();
        }
        return INSTANCE;
    }

    private Show() {
        availableSeats = new HashSet<>();
        availableSeats.add("1A");
        availableSeats.add("1B");
    }

    public synchronized boolean bookSeat(String seat) {
        return availableSeats.remove(seat);
    }
}
