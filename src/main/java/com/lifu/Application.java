
package com.lifu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}

	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
	     return application.sources(Application.class);  
	 }  
}
