package com.fplusf.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageApiApplication implements Runnable {

	public static void main(String[] args) {
		SpringApplication.run(MessageApiApplication.class, args);
	}

	@Override
	public void run() {
		System.out.println("============>");
		System.out.println("============>");
		System.out.println("============>CCC");
		System.out.println("============>CCC");
	}

}
