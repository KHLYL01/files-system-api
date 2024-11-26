package com.conquer_team.files_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
//@EnableAsync
@EnableCaching
public class FilesSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesSystemApplication.class, args);
    }

}
