package com.zzjz.zkemlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.zzjz.zkemlink.mapper")
public class ZkemlinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkemlinkApplication.class, args);
    }
}
