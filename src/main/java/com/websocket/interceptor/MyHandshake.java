package com.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description
 * @auther 冉长冬  拦截器实现
 * @create 2019-05-26 14:01
 */
@Slf4j
@Service
public class MyHandshake implements HandshakeInterceptor {

    /**
     * @Author 冉长冬
     * @Description  握手之前干啥，常用来注册用户信息，绑定 WebSocketSession
     * @Date  2019/5/26 14:02
     * @Param [serverHttpRequest, serverHttpResponse, webSocketHandler, map] 
     * @return boolean
     **/
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            // 从session中获取到当前登录的用户信息. 作为socket的账号信息. session的的WEBSOCKET_USERNAME信息,在用户打开页面的时候设置.
            String userName = (String) servletRequest.getSession().getAttribute("WEBSOCKET_USERNAME");
            map.put("WEBSOCKET_USERNAME", userName);
        }
        return true;
    }

    /**
     * @Author 冉长冬
     * @Description  握手之后干啥
     * @Date  2019/5/26 14:02
     * @Param [serverHttpRequest, serverHttpResponse, webSocketHandler, e]
     * @return void
     **/
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
