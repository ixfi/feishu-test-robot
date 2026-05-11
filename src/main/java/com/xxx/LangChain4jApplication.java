package com.xxx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LangChain4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(LangChain4jApplication.class, args);
        System.out.println("===== 服务启动成功，访问接口测试 =====");
    }
}