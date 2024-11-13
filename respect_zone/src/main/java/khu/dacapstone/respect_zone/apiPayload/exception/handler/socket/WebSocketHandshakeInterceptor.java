package khu.dacapstone.respect_zone.apiPayload.exception.handler.socket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import khu.dacapstone.respect_zone.service.SpeechQueryService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final SpeechQueryService speechQueryService;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) throws Exception {

        // URL 파라미터에서 speechId 가져오기
        String query = request.getURI().getQuery();
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        // deviceId는 STOMP 연결 시 헤더로 전달될 예정이므로 여기서는 체크하지 않음
        String speechId = queryParams.get("speechId");
        if (speechId == null) {
            throw new WebSocketHandshakeException("speechId는 필수입니다.");
        }

        try {
            Long speechIdLong = Long.parseLong(speechId);
            // speechId만 attributes에 저장
            attributes.put("speechId", speechIdLong);
            return true;
        } catch (NumberFormatException e) {
            throw new WebSocketHandshakeException("speechId가 유효한 숫자 형식이 아닙니다.");
        }
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception) {
    }
}