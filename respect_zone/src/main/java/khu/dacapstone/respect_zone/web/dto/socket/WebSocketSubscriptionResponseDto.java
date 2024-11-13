package khu.dacapstone.respect_zone.web.dto.socket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebSocketSubscriptionResponseDto {
    private Long speechId;
    private String destination;
    private String subscriptionId;
    private String message;
}