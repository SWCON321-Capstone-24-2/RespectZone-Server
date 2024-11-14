package khu.dacapstone.respect_zone.apiPayload.exception.handler.socket;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.web.dto.socket.WebSocketSubscriptionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

        private final SimpMessagingTemplate messagingTemplate;

        @EventListener
        public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
                Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

                if (sessionAttributes == null) {
                        log.error("Session attributes is null");
                        return;
                }

                String deviceId = headerAccessor.getFirstNativeHeader("deviceId");
                Long speechId = (Long) sessionAttributes.get("speechId");
                String destination = headerAccessor.getDestination();

                if (destination == null) {
                        log.error("Destination is null");
                        return;
                }

                // 구독 성공 응답은 실제 구독한 destination에 대해서만 보냄
                if (destination.startsWith("/topic/speech/")) {
                        WebSocketSubscriptionResponseDto responseDto = WebSocketSubscriptionResponseDto.builder()
                                        .speechId(speechId)
                                        .destination(destination)
                                        .subscriptionId(headerAccessor.getSubscriptionId())
                                        .message("구독이 성공했습니다.")
                                        .build();

                        ApiResponse<WebSocketSubscriptionResponseDto> response = ApiResponse.onSuccess(responseDto);

                        // 구독 성공 응답을 클라이언트에게 전송
                        messagingTemplate.convertAndSendToUser(
                                        headerAccessor.getSessionId(),
                                        "/queue/subscribe",
                                        response);

                        // 로깅 추가
                        log.info("Subscription successful - Device: {}, Speech: {}, Destination: {}",
                                        deviceId, speechId, destination);
                }
        }
}