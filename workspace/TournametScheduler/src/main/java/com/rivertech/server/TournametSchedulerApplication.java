package com.rivertech.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/***
 * Tournament scheduler class. 
 * @author Omar Zammit
 */
@ComponentScan("com.rivertech.controller")
@SpringBootApplication
public class TournametSchedulerApplication {

	/***
	 * Main method of API application.
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TournametSchedulerApplication.class, args);
	}

}
