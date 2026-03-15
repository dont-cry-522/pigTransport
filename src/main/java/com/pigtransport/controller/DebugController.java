package com.pigtransport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
public class DebugController {

    @GetMapping("/debug/request")
    public String debugRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>请求信息调试</h1>");

        sb.append("<h3>请求信息:</h3>");
        sb.append("<p>URI: ").append(request.getRequestURI()).append("</p>");
        sb.append("<p>URL: ").append(request.getRequestURL()).append("</p>");
        sb.append("<p>方法: ").append(request.getMethod()).append("</p>");
        sb.append("<p>路径: ").append(request.getServletPath()).append("</p>");

        sb.append("<h3>头部信息:</h3>");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            sb.append("<p>").append(header).append(": ").append(request.getHeader(header)).append("</p>");
        }

        sb.append("<h3>测试链接:</h3>");
        sb.append("<ul>");
        sb.append("<li><a href='/login.html'>直接访问 login.html</a></li>");
        sb.append("<li><a href='/'>访问根路径 /</a></li>");
        sb.append("<li><a href='/debug/interceptor'>检查拦截器状态</a></li>");
        sb.append("<li><a href='/api/test'>测试API</a></li>");
        sb.append("</ul>");

        return sb.toString();
    }


    @GetMapping("/debug/files")
    public String debugFiles() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>文件检查</h1>");

        // 检查关键文件
        String[] files = {
                "static/login.html",
                "static/layui/css/layui.css",
                "static/index.html"
        };

        for (String file : files) {
            sb.append("<h3>").append(file).append("</h3>");
            try {
                ClassPathResource resource = new ClassPathResource(file);
                if (resource.exists()) {
                    sb.append("<p style='color:green'>✅ 文件存在</p>");
                    sb.append("<p>路径: ").append(resource.getURL()).append("</p>");
                } else {
                    sb.append("<p style='color:red'>❌ 文件不存在</p>");
                }
            } catch (Exception e) {
                sb.append("<p style='color:red'>❌ 检查错误: ").append(e.getMessage()).append("</p>");
            }
        }

        return sb.toString();
    }
}