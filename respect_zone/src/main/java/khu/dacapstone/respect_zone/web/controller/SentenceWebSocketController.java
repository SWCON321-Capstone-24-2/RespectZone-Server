package khu.dacapstone.respect_zone.web.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.service.SentenceAnalysisService;
import khu.dacapstone.respect_zone.service.SpeechQueryService;
import khu.dacapstone.respect_zone.web.dto.SentenceAnalysisResponseDto;
import khu.dacapstone.respect_zone.web.dto.SentenceMessageDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SentenceWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SpeechQueryService speechQueryService;
    private final SentenceAnalysisService sentenceAnalysisService;

    @MessageMapping("/analyze")
    public void analyzeSentence(@Payload SentenceMessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // deviceId는 헤더에서, speechId는 세션 attributes에서 가져옴
            String deviceId = headerAccessor.getFirstNativeHeader("deviceId");
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

            if (sessionAttributes == null) {
                throw new IllegalStateException("세션 정보를 찾을 수 없습니다.");
            }

            Long speechId = (Long) sessionAttributes.get("speechId");

            if (deviceId == null) {
                throw new IllegalStateException("deviceId가 헤더에 없습니다.");
            }

            if (speechId == null) {
                throw new IllegalStateException("speechId를 찾을 수 없습니다.");
            }

            // Speech 존재 여부와 deviceId 일치 여부 확인
            Speech speech = speechQueryService.getSpeech(speechId, deviceId);

            // Kafka로 메시지 전송
            sentenceAnalysisService.analyzeSentence(
                    messageDto.getSentence(),
                    speech.getId(),
                    messageDto.getTimestamp().toString());

        } catch (Exception e) {
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            Long speechId = 0L;

            if (sessionAttributes != null) {
                speechId = (Long) sessionAttributes.get("speechId");
                if (speechId == null) {
                    speechId = 0L;
                }
            }

            SentenceAnalysisResponseDto errorResponse = SentenceAnalysisResponseDto.builder()
                    .speechId(speechId)
                    .message("문장 분석 요청 중 오류가 발생했습니다: " + e.getMessage())
                    .build();

            ApiResponse<SentenceAnalysisResponseDto> apiResponse = ApiResponse.onFailure(
                    "500",
                    "문장 분석 요청 실패",
                    errorResponse);

            messagingTemplate.convertAndSend(
                    "/topic/speech/" + speechId + "/error",
                    apiResponse);
        }
    }
}