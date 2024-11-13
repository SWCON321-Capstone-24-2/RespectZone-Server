package khu.dacapstone.respect_zone.web.dto.socket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebSocketConnectionResponseDto {
    private Long speechId;
    private String deviceId;
    private String connectionId;
    private String message;
}