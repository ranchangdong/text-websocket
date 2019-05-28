package com.websocket.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @auther 冉长冬
 * @create 2019-05-26 14:04
 */
@Slf4j
@Service
public class MyHandler implements WebSocketHandler {


    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
    private final static List<WebSocketSession> SESSIONS = Collections.synchronizedList(new ArrayList<>());


    /**
     * @Author 冉长冬
     * @Description  连接建立之后
     * @Date  2019/5/26 14:07
     * @Param [webSocketSession] 
     * @return void
     **/
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("链接成功......");

        SESSIONS.add(webSocketSession);
        String userName = (String) webSocketSession.getAttributes().get("WEBSOCKET_USERNAME");
        if (userName != null) {
            JSONObject obj = new JSONObject();
            // 统计一下当前登录系统的用户有多少个
            obj.put("count", SESSIONS.size());
            users(obj);
            webSocketSession.sendMessage(new TextMessage(obj.toJSONString()));
        }
    }

    /**
     * @Author 冉长冬
     * @Description  处理消息
     * @Date  2019/5/26 14:07
     * @Param [webSocketSession, webSocketMessage] 
     * @return void
     **/
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        log.info("处理要发送的消息");
        JSONObject msg = JSON.parseObject(webSocketMessage.getPayload().toString());
        JSONObject obj = new JSONObject();
        if (msg.getInteger("type") == 1) {
            //给所有人
            obj.put("msg", msg.getString("msg"));
            sendMessageToUsers(new TextMessage(obj.toJSONString()));
        } else {
            //给个人
            String to = msg.getString("to");
            obj.put("msg", msg.getString("msg"));
            sendMessageToUser(to, new TextMessage(obj.toJSONString()));
        }

    }

    /**
     * @Author 冉长冬
     * @Description  处理传输错误
     * @Date  2019/5/26 14:07
     * @Param [webSocketSession, throwable] 
     * @return void
     **/
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()){
            webSocketSession.close();
        }
        log.info("链接出错，关闭链接......");
        SESSIONS.remove(webSocketSession);
    }

    /**
     * @Author 冉长冬
     * @Description  连接关闭后
     * @Date  2019/5/26 14:08
     * @Param [webSocketSession, closeStatus] 
     * @return void
     **/
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.info("链接关闭......" + closeStatus.toString());
        SESSIONS.remove(webSocketSession);
    }

    /**
     * @Author 冉长冬
     * @Description  是否处理分片消息
     * @Date  2019/5/26 14:08
     * @Param [] 
     * @return boolean
     **/
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    
    /**
     * @Author 冉长冬
     * @Description  给所有在线用户发送消息
     * @Date  2019/5/26 14:11
     * @Param [message] 
     * @return void
     **/
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Author 冉长冬
     * @Description  给某个用户发送消息
     * @Date  2019/5/26 14:11
     * @Param [userName, message] 
     * @return void
     **/
    public void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * @Author 冉长冬
     * @Description  将系统中的用户传送到前端
     * @Date  2019/5/26 14:11
     * @Param [obj] 
     * @return void
     **/
    private void users(JSONObject obj) {
        List<String> userNames = new ArrayList<>();
        for (WebSocketSession webSocketSession : SESSIONS) {
            userNames.add((String) webSocketSession.getAttributes().get("WEBSOCKET_USERNAME"));
        }
        obj.put("users", userNames);
    }

}
