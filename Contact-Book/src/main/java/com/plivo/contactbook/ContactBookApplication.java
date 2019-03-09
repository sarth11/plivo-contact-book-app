package com.plivo.contactbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ContactBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactBookApplication.class, args);
	}

}
