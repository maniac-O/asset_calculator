package com.SH.asset_calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AssetCalculatorApplicationTests {

	@Autowired
	DefaultListableBeanFactory bf;

	@Test
	void contextLoads() {
		System.out.println(bf.getBean("ExceptionHandlerExceptionResolver"));
	}

}
