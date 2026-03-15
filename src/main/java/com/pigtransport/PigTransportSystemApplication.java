// src/main/java/com/pigtransport/PigTransportSystemApplication.java
package com.pigtransport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 添加扫描路径
@SpringBootApplication(scanBasePackages = {"com.pigtransport", "com.pigtransport.config"})
public class PigTransportSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigTransportSystemApplication.class, args);
        System.out.println("猪只运输管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
    }
}