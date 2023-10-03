package com.hot6.pnureminder.config;

import com.hot6.pnureminder.handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 클라이언트가 ws://localhost:8080/chat으로 커넥션을 연결하고 통신할 수 있게 하는 작업
        registry.addHandler(chatHandler, "ws/chat").setAllowedOrigins("*");
    }
}