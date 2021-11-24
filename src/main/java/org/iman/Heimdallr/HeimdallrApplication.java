package org.iman.Heimdallr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeimdallrApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeimdallrApplication.class, args);
	}

	// YONG Debug mode
}
