package com.toby.controller;


import com.alibaba.fastjson.JSON;
import com.toby.model.TokenModel;
import com.toby.services.EquipService;
import com.toby.services.TokenManager;
import com.toby.utils.ApplicationContextRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/currentMap/{uid}")
@Component
public class CurrentMapWebSocket {

    private static Logger log = LoggerFactory.getLogger(CurrentMapWebSocket.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Map，用来存放每个客户端对应的Session对象。
    private static final Map<Session, String> CurrentSessionMap = new ConcurrentHashMap<>();
    private static ScheduledExecutorService sendService = null;

    private Runnable runnable = new Runnable() {
        public void run() {
            ApplicationContext act = ApplicationContextRegister.getApplicationContext();
            EquipService equipService = act.getBean(EquipService.class);
            String infoStr = JSON.toJSONString(equipService.getAllEquips());
            //log.info("地图视图下发数据：{}", infoStr);
            BroadCastInfo(infoStr);
        }
    };

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid")String uid) {
        ApplicationContext act = ApplicationContextRegister.getApplicationContext();
        TokenManager tokenManager = act.getBean(TokenManager.class);
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
        CurrentSessionMap.put(session, uid);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("地图视图有连接加入，当前连接数为：{}", cnt);
        //SendMessage(session, "连接成功");
        if (cnt != 0 && sendService == null) {
            log.info("new SendThread：{}", sendService);
            sendService = Executors.newSingleThreadScheduledExecutor();
            sendService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        if (CurrentSessionMap.containsKey(session)) {
            CurrentSessionMap.remove(session);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
            if (cnt == 0) {
                sendService.shutdownNow();
                sendService = null;
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        if (CurrentSessionMap.containsKey(session)) {
            CurrentSessionMap.remove(session);
            int cnt = OnlineCount.decrementAndGet();
            if (cnt == 0) {
                sendService.shutdownNow();
                sendService = null;
            }
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    private static void SendMessage(Session session, String message) {
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
    private static void BroadCastInfo(String message)  {
        for (Session session : CurrentSessionMap.keySet()) {
            if(session.isOpen()) {
                SendMessage(session, message);
            }
        }
    }
}
