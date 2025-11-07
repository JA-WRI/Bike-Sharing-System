package com.veloMTL.veloMTL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VeloMtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeloMtlApplication.class, args);
	}

}
