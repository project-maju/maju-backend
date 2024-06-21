package org.duckdns.omaju.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.duckdns.omaju.core")
@ComponentScan("org.duckdns.omaju.api")
public class OmajuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmajuApiApplication.class, args);
	}

}
