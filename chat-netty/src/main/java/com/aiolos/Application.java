package com.aiolos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Aiolos
 * @date 2019-03-18 23:34
 */
@SpringBootApplication(scanBasePackages = {"com.aiolos", "org.n3r.idworker"})
@MapperScan(basePackages = "com.aiolos.dao")
// 扫描所有需要的包，包含一些自用的工具类所在的路径
public class Application {

    // springboot启动时注册SpringUtil
    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
