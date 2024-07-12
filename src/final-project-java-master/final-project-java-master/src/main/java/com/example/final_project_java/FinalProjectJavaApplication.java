package com.example.final_project_java;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class FinalProjectJavaApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.info("현재 시각: {}", LocalDateTime.now());
	}


	public static void main(String[] args) {
		SpringApplication.run(FinalProjectJavaApplication.class, args);
	}

}
