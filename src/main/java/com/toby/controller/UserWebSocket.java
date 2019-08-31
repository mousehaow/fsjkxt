package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.model.TokenModel;
import com.toby.services.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/user/{uid}")
@Component
public class UserWebSocket {

    private static Logger log = LoggerFactory.getLogger(DataUploadWebSocket.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Map，用来存放每个客户端对应的Session对象。
    private static final Map<Session, String> SessionMap = new ConcurrentHashMap<>();

    @Autowired
    private TokenManager tokenManager;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        TokenModel token = tokenManager.getToken(uid);
        if (token == null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!tokenManager.checkToken(token)) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        SessionMap.put(session, uid + "_" + token.getUserId());
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
        //SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        if (SessionMap.containsKey(session)) {
            SessionMap.remove(session);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
        //SendMessage(session, "收到消息，消息内容："+message);

    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        if (SessionMap.containsKey(session)) {
            SessionMap.remove(session);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionMap.keySet()) {
            if(session.isOpen()){
                SendMessage(session, message);
            }
        }
    }

    @JmsListener(destination = "fsjkxt.topic")
    public void receiveTopic(String text) {
        JSONObject jsonObject = JSON.parseObject(text);
        if (jsonObject.containsKey("userOffline")) {
            String tokenAndId = jsonObject.getString("userOffline");
            String[] param = tokenAndId.split("_");
            String token = param[0];
            String userId = param[1];
            Session session = null;
            for (Session s : SessionMap.keySet()) {
                if(SessionMap.get(s).equals(token)){
                    session = s;
                    break;
                }
            }
            if(session!=null){
                SendMessage(session, "logout");
                try {
                    session.close();
                    if (SessionMap.containsKey(session)) {
                        SessionMap.remove(session);
                        OnlineCount.decrementAndGet();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                log.warn("没有找到你指定ID的会话：{}",userId);
            }
        }

    }
}
