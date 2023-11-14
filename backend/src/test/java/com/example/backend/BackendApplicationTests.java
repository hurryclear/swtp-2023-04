package com.example.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BackendApplicationTests {

	BackendApplication.Calculator calculatorTest = new BackendApplication.Calculator();

	@Test
	public void test_sum() {
		int a = 1;
		int b = 2;
		int expectedSum = 3;

		int actualSum = calculatorTest.sum(a, b);

		Assertions.assertEquals(expectedSum, actualSum);
	}

}
