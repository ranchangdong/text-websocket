package com.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @auther 冉长冬
 * @create 2019-05-26 14:23
 */
@Controller
@RequestMapping("/socket")
public class WebSocketController {
    
    
    /**
     * @Author 冉长冬
     * @Description  第一个用户
     * @Date  2019/5/26 14:24
     * @Param [request] 
     * @return java.lang.String
     **/
    @RequestMapping("/chat1")
    public String chat1(HttpServletRequest request) {
        // 假设用户tom登录,存储到session中
         System.out.println("---");
        request.getSession().setAttribute("WEBSOCKET_USERNAME", "tom");
        return "chat1";
    }

    /**
     * @Author 冉长冬
     * @Description  第二个用户
     * @Date  2019/5/26 14:24
     * @Param [request]
     * @return java.lang.String
     **/
    @RequestMapping("/chat2")
    public String chat2(HttpServletRequest request) {
        // 假设用户jerry登录,存储到session中
        request.getSession().setAttribute("WEBSOCKET_USERNAME", "jerry");
        return "chat2";
    }

    @RequestMapping("/chat3")
    public String chat3(HttpServletRequest request) {
        // 假设用户jack登录,存储到session中
        request.getSession().setAttribute("WEBSOCKET_USERNAME","jack");
        return "chat3";
    }
}
