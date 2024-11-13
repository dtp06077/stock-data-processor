package com.openapi.demo.websocket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(100);
    }
}
