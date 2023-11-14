package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static class Calculator {
		public int sum(int a, int b) {
			return a + b;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
