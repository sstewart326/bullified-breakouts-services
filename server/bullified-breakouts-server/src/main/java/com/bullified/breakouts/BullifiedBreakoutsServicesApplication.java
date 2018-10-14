package com.bullified.breakouts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bullified.breakouts")
public class BullifiedBreakoutsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BullifiedBreakoutsServicesApplication.class, args);
	}
}
