package com.aiolos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Aiolos
 * @date 2019-03-18 23:34
 */
@SpringBootApplication(scanBasePackages = {"com.aiolos"})
@MapperScan(basePackages = "com.aiolos.dao")
// 扫描所有需要的包，包含一些自用的工具类所在的路径
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
