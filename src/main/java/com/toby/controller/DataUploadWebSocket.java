package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.model.DetailModel;
import com.toby.model.RecordModel;
import com.toby.services.DetailService;
import com.toby.services.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/upload")
@Component
public class DataUploadWebSocket {

    private static Logger log = LoggerFactory.getLogger(DataUploadWebSocket.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Map，用来存放每个客户端对应的Session对象。
    private static final Map<Session, String> SessionMap = new ConcurrentHashMap<>();

    static final Map<String, String> CurrentRecordMap = new ConcurrentHashMap<>();

    @Autowired
    private RecordService recordService;

    @Autowired
    private DetailService detailService;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        String recordID = recordService.createNewRecord(new RecordModel());
        SessionMap.put(session, recordID);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
        SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        if (SessionMap.containsKey(session)) {
            String recordId = SessionMap.remove(session);
            CurrentRecordMap.remove(recordId);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
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
        JSONObject jsonObject = JSON.parseObject(message);
        String recordId = SessionMap.get(session);
        if (recordId == null || recordId.equals("")) {
            return;
        }
        if (jsonObject.getString("type").equals("recordInit")) {
            RecordModel record = JSON.parseObject(jsonObject.getString("data"), RecordModel.class);
            record.setId(recordId);
            recordService.updateRecord(record);
        }
        if (jsonObject.getString("type").equals("detailInsert")) {
            DetailModel detail = JSON.parseObject(jsonObject.getString("data"), DetailModel.class);
            detail.setRecordId(recordId);
            detailService.addNewDetail(detail);
            CurrentRecordMap.put(recordId, jsonObject.getString("data"));
        }
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
            String recordId = SessionMap.remove(session);
            CurrentRecordMap.remove(recordId);
            OnlineCount.decrementAndGet();
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
}
