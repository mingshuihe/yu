package com.helo.ming.yu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.helo.ming.yu.**.dao")
@SpringBootApplication
public class YuApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuApplication.class, args);
	}

}
