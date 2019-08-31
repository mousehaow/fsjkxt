package com.toby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class FsjkxtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FsjkxtApplication.class, args);
	}

}
