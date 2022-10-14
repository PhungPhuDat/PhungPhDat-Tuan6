package com.example.ActiveMQ_Recevier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = ActiveMQAutoConfiguration.class)
public class ActiveMqRecevierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveMqRecevierApplication.class, args);
	}

}
