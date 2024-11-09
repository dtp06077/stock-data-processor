package com.openapi.demo.websocket.handler;

import com.openapi.demo.common.KisConstant;
import com.openapi.demo.transfer.JsonTransfer;
import com.openapi.demo.security.token.ApprovalKeyManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

@Component
@RequiredArgsConstructor
public class StockWebSocketHandler extends TextWebSocketHandler {

    private final ApprovalKeyManager approvalKeyManager;
    private final WebSocketClient webSocketClient;
    private final ExecutorService executorService;
    private BlockingDeque<String> messageQueue;

    @PostConstruct
    public void connectToWebSocket() {
        String approvalKey = approvalKeyManager.getApprovalKey();
        String url = KisConstant.WS_BASE_URL+"/tryitout/H0STCNT0";

        messageQueue = new LinkedBlockingDeque<>();

        TextWebSocketHandler textWebSocketHandler = new TextWebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {

                String[] itemCodeList = new String[]{"098120", "131100", "009520", "095570", "006840", "282330", "027410", "138930", "001465", "001460"};

                // 각 종목 코드에 대해 스레드에서 요청 전송
                for (String itemCode : itemCodeList) {
                    String subscribeMessage = JsonTransfer.getExePriceMessage(approvalKey, itemCode);
                    messageQueue.offer(subscribeMessage);
                }

                executorService.submit(() -> {
                    while (!messageQueue.isEmpty()) {
                        try {
                            String message = messageQueue.take(); // 큐에서 메시지 가져오기
                            session.sendMessage(new TextMessage(message)); // 메시지 전송
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {

                System.out.println("Received message: " + message.getPayload());

//                Properties props = new Properties();
//                props.put("bootstrap.servers", "118.67.129.24:9092");
//                props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//                KafkaProducer<String, String> producer = new KafkaProducer<>(props);
//
//                ProducerRecord<String, String> record = new ProducerRecord<>("test", message.getPayload());
//
//
//                producer.send(record, (metadata, exception) -> {
//                    if (exception != null) {
//
//                        System.err.println("Error while producing message: " + exception.getMessage());
//                    } else {
//
//                        System.out.printf("Message sent to topic %s with offset %d%n", metadata.topic(), metadata.offset());
//                    }
//                });
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                // 연결 종료 시 처리
                System.out.println("WebSocket connection closed: " + status);
            }
        };

        // 웹소켓 연결
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(webSocketClient, textWebSocketHandler, url);
        connectionManager.start(); // 연결 시작
    }
}
