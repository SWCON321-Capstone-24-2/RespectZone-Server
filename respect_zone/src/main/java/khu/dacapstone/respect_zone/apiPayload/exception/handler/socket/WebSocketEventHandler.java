package khu.dacapstone.respect_zone.apiPayload.exception.handler.socket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.web.dto.socket.WebSocketConnectionResponseDto;
import khu.dacapstone.respect_zone.web.dto.socket.WebSocketSubscriptionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

        private final SimpMessageSendingOperations messagingTemplate;

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event) {
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
                String deviceId = headerAccessor.getFirstNativeHeader("deviceId");
                Long speechId = (Long) headerAccessor.getSessionAttributes().get("speechId");

                WebSocketConnectionResponseDto responseDto = WebSocketConnectionResponseDto.builder()
                                .speechId(speechId)
                                .deviceId(deviceId)
                                .connectionId(headerAccessor.getSessionId())
                                .message("WebSocket 연결이 성공했습니다.")
                                .build();

                ApiResponse<WebSocketConnectionResponseDto> response = ApiResponse.onSuccess(responseDto);

                // 연결된 클라이언트에게만 성공 응답 전송
                messagingTemplate.convertAndSendToUser(
                                headerAccessor.getSessionId(),
                                "/queue/connect",
                                response);
        }

        @EventListener
        public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
                String deviceId = headerAccessor.getFirstNativeHeader("deviceId");
                Long speechId = (Long) headerAccessor.getSessionAttributes().get("speechId");
                String destination = headerAccessor.getDestination();

                // 구독 성공 응답은 실제 구독한 destination에 대해서만 보냄
                if (destination != null && destination.startsWith("/topic/speech/")) {

                        WebSocketSubscriptionResponseDto responseDto = WebSocketSubscriptionResponseDto.builder()
                                        .speechId(speechId)
                                        .destination(destination)
                                        .subscriptionId(headerAccessor.getSubscriptionId())
                                        .message("구독이 성공했습니다.")
                                        .build();

                        ApiResponse<WebSocketSubscriptionResponseDto> response = ApiResponse.onSuccess(responseDto);

                        // 구독한 클라이언트에게만 ApiResponse 형식으로 성공 응답 전송
                        messagingTemplate.convertAndSendToUser(
                                        headerAccessor.getSessionId(),
                                        "/queue/subscribe",
                                        response);
                }
        }
}