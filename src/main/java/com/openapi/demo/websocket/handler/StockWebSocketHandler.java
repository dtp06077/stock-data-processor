package com.openapi.demo.websocket.handler;

import com.openapi.demo.common.KisConstant;
import com.openapi.demo.kafka.KafkaProducerService;
import com.openapi.demo.transfer.JsonTransfer;
import com.openapi.demo.security.token.ApprovalKeyManager;
import com.openapi.demo.transfer.ListTransfer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

@Component
@RequiredArgsConstructor
public class StockWebSocketHandler extends TextWebSocketHandler {

    private final ApprovalKeyManager approvalKeyManager;
    private final WebSocketClient webSocketClient;
    private final ExecutorService executorService;
    private final ListTransfer listTransfer;
    private final KafkaProducerService kafkaProducerService;

    private BlockingDeque<String> messageQueue;

    @PostConstruct
    public void connectToWebSocket() {
        String approvalKey = approvalKeyManager.getApprovalKey();
        String url = KisConstant.WS_BASE_URL+KisConstant.GET_REAL_TIME_EXE_PRICE_PATH;

        messageQueue = new LinkedBlockingDeque<>();

        TextWebSocketHandler textWebSocketHandler = new TextWebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {

                List<String> itemCodeList = listTransfer.getItemCodeList();

                for (String itemCode : itemCodeList) {
                    String subscribeMessage = JsonTransfer.getExePriceMessage(approvalKey, itemCode);
                    messageQueue.offer(subscribeMessage);
                }

                executorService.submit(() -> {

                    while (!messageQueue.isEmpty()) {
                        try {
                            String message = messageQueue.take();
                            session.sendMessage(new TextMessage(message));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                });
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {

                //System.out.println("Received message: " + message.getPayload());
                kafkaProducerService.sendMessage("test", message.getPayload());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                // 연결 종료 시 처리
                System.out.println("WebSocket connection closed: " + status);
                executorService.close();
            }
        };

        // 웹소켓 연결
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(webSocketClient, textWebSocketHandler, url);
        connectionManager.start(); // 연결 시작
    }
}
