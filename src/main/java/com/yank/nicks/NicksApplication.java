package com.yank.nicks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.yank.nicks.controller"})
public class NicksApplication {

	public static void main(String[] args) {
		SpringApplication.run(NicksApplication.class, args);
	}

}
