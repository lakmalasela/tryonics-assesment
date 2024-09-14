package com.logmaven.exmaven;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.logmaven.exmaven"})
public class ExmavenApplication {


	public static void main(String[] args) {
		SpringApplication.run(ExmavenApplication.class, args);
	}


}
