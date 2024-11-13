package khu.dacapstone.respect_zone.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
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
            Long speechId = (Long) headerAccessor.getSessionAttributes().get("speechId");

            if (deviceId == null) {
                throw new IllegalStateException("deviceId가 헤더에 없습니다.");
            }

            if (speechId == null) {
                throw new IllegalStateException("speechId를 찾을 수 없습니다.");
            }

            // Speech 존재 여부와 deviceId 일치 여부 확인
            Speech speech = speechQueryService.getSpeech(speechId, deviceId);

            // 문장 분석 수행
            SentenceType analyzedType = sentenceAnalysisService.analyzeSentence(messageDto.getSentence());

            // 분석 결과를 클라이언트에게 전송
            SentenceAnalysisResponseDto responseDto = SentenceAnalysisResponseDto.builder()
                    .speechId(speech.getId())
                    .sentence(messageDto.getSentence())
                    .sentenceType(analyzedType)
                    .message("문장 분석이 완료되었습니다.")
                    .build();

            ApiResponse<SentenceAnalysisResponseDto> apiResponse = ApiResponse.onSuccess(responseDto);

            messagingTemplate.convertAndSend(
                    "/topic/speech/" + speech.getId() + "/analysis",
                    apiResponse);

        } catch (Exception e) {
            Long speechId = (Long) headerAccessor.getSessionAttributes().get("speechId");
            if (speechId == null) {
                speechId = 0L;
            }

            SentenceAnalysisResponseDto errorResponse = SentenceAnalysisResponseDto.builder()
                    .speechId(speechId)
                    .message("문장 분석 중 오류가 발생했습니다: " + e.getMessage())
                    .build();

            ApiResponse<SentenceAnalysisResponseDto> apiResponse = ApiResponse.onFailure(
                    "500",
                    e.getMessage(),
                    errorResponse);

            messagingTemplate.convertAndSend(
                    "/topic/speech/" + speechId + "/error",
                    apiResponse);
        }
    }
}