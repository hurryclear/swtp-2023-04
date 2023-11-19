package com.example.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	BackendApplication.Calculator test = new BackendApplication.Calculator();
	@Test
	public void test_sum() {
		int a = 1;
		int b = 2;
		int expected = 3;
		int actual = test.sum(a, b);
		Assertions.assertEquals(expected, actual);
	}


}
