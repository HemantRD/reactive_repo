package com.example.demo.stream.reactive.book;

/**
 * All the examples of the book implement this.
 * It introduces a name and cahpter number for the example.
 * 
 * @author meddle
 */
public interface Program {

	String name();

	int chapter();

	void run();
}