package com.tunnel.monitor.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/data/{userId}")
@ApplicationScoped
public class DataPushWebSocket {

    private static final Logger log = LoggerFactory.getLogger(DataPushWebSocket.class);

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessions.put(userId, session);
        log.info("WebSocket 连接建立: userId={}, sessionId={}", userId, session.getId());
        sendMessage(userId, "连接成功，等待数据推送...");
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) {
        log.info("收到客户端消息: userId={}, message={}", userId, message);
        if ("ping".equalsIgnoreCase(message)) {
            sendMessage(userId, "pong");
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        sessions.remove(userId);
        log.info("WebSocket 连接关闭: userId={}, sessionId={}", userId, session.getId());
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
        log.error("WebSocket 错误: userId={}, sessionId={}, error={}", userId, session.getId(), throwable.getMessage());
    }

    public void sendMessage(String userId, String message) {
        Session session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("发送消息失败: userId={}, error={}", userId, e.getMessage());
            }
        }
    }

    public void broadcast(String message) {
        sessions.forEach((userId, session) -> {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("广播消息失败: userId={}, error={}", userId, e.getMessage());
                }
            }
        });
    }

    public void sendAlarm(String userId, Object data) {
        String message = "{\"type\":\"ALARM\",\"data\":" + data.toString() + "}";
        sendMessage(userId, message);
    }

    public void broadcastAlarm(Object data) {
        String message = "{\"type\":\"ALARM\",\"data\":" + data.toString() + "}";
        broadcast(message);
    }

    public void sendDeviceData(String userId, Object data) {
        String message = "{\"type\":\"DEVICE_DATA\",\"data\":" + data.toString() + "}";
        sendMessage(userId, message);
    }

    public int getOnlineCount() {
        return (int) sessions.values().stream().filter(Session::isOpen).count();
    }
}
