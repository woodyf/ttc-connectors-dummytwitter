package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableActivitiCloudConnector
@EnableScheduling
public class TwitterCloudConnectorApp {

	public static void main(String[] args) {
		SpringApplication.run(TwitterCloudConnectorApp.class, args);
	}
}
