package org.duckdns.omaju.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.duckdns.omaju.core")
@ComponentScan("org.duckdns.omaju.batch")
public class OmajuBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmajuBatchApplication.class, args);
	}

}
