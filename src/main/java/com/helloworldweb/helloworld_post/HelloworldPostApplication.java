package com.helloworldweb.helloworld_post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HelloworldPostApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldPostApplication.class, args);
	}

}
