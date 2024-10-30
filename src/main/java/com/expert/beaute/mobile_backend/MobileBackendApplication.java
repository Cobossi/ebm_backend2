package com.expert.beaute.mobile_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MobileBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileBackendApplication.class, args);
	}

}
