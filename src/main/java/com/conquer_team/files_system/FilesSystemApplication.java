package com.conquer_team.files_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class FilesSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesSystemApplication.class, args);
    }
//
//    @Scheduled(cron = "*/5 * * * * *")
//    public void cronTest(){
//        int i=0;
//        System.out.println("ameen"+i+1);
//        i++;
//    }

}
