//package com.pigtransport.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class IndexController {
//
//    /**
//     * 根路径 -> 直接跳转到登录页
//     */
//    @GetMapping("/")
//    public String root() {
//        // 你的登录页在 static/login.html
//        return "forward:/login.html";
//    }
//
//    /**
//     * 登录页面
//     */
//    @GetMapping("/login")
//    public String login() {
//        return "forward:/login.html";
//    }
//
//    /**
//     * 主页（登录后）
//     */
//    @GetMapping("/home")
//    public String home() {
//        return "forward:/index.html";
//    }
//
//    /**
//     * 消毒记录页面
//     */
//    @GetMapping("/disinfection")
//    public String disinfection() {
//        return "forward:/disinfection-list.html";
//    }
//
//    /**
//     * 位置签到页面
//     */
//    @GetMapping("/location")
//    public String location() {
//        return "forward:/location-list.html";
//    }
//
//    /**
//     * 用户管理页面
//     */
//    @GetMapping("/user")
//    public String user() {
//        return "forward:/user-list.html";
//    }
//
//    /**
//     * 车辆管理页面
//     */
//    @GetMapping("/vehicle")
//    public String vehicle() {
//        return "forward:/vehicle-list.html";
//    }
//
//    /**
//     * 批次管理页面
//     */
//    @GetMapping("/batch")
//    public String batch() {
//        return "forward:/batch-list.html";
//    }
//
//    /**
//     * 任务管理页面
//     */
//    @GetMapping("/task")
//    public String task() {
//        return "forward:/task-list.html";
//    }
//}

package com.pigtransport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /**
     * 根路径 -> 登录页
     */
    @GetMapping("/")
    public String index() {
        return "forward:/login.html";
    }
}