package com.codelitt.technical.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of the application that is annotated with `@SpringBootApplication` to enable
 * the automatic configuration and component scan for a Spring Boot application.
 *
 *  @author Samuel Catalano
 */
@SpringBootApplication
public class Application {

	/**
	 * The entry point of the application. It uses the `SpringApplication.run` method to launch the application.
	 * @param args The command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
