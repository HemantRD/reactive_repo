package com.example.demo.stream.reactive.book;

public interface CheckedAction1<T> {
	void call(T arg) throws Exception;
}