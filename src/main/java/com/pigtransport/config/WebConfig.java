package com.pigtransport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录
        String projectRoot = System.getProperty("user.dir");

        // 映射 /img/** 到项目根目录下的 img 文件夹
        String imgPath = "file:" + projectRoot + File.separator + "img" + File.separator;

        System.out.println("配置图片资源映射：");
        System.out.println("URL路径：/img/**");
        System.out.println("物理路径：" + imgPath);

        registry.addResourceHandler("/img/**")
                .addResourceLocations(imgPath);

        // 如果需要，也可以映射其他目录
        String uploadPath = "file:" + projectRoot + File.separator + "upload" + File.separator;
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath);
    }
}