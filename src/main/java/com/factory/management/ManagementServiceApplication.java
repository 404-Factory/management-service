package com.factory.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.factory.management")
@EntityScan(basePackages = "com.factory.management")
public class ManagementServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ManagementServiceApplication.class, args);
	}

}


