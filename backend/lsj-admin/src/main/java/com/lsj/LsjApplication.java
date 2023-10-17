package com.lsj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * program start
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LsjApplication {
    public static void main(String[] args) {
         System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(LsjApplication.class, args);
        System.out.println("농산물 관리 시스템-The backend runs successfully");
    }
}
