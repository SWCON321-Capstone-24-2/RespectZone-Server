package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import khu.dacapstone.respect_zone.repository.SpeechRepository;
import khu.dacapstone.respect_zone.web.dto.SentenceAnalysisResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private final SentenceCommandService sentenceCommandService;
    private final SpeechRepository speechRepository;

    public KafkaConsumerService(
            SimpMessagingTemplate messagingTemplate,
            ObjectMapper objectMapper,
            SentenceCommandService sentenceCommandService,
            SpeechRepository speechRepository) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
        this.sentenceCommandService = sentenceCommandService;
        this.speechRepository = speechRepository;
    }

    @KafkaListener(topics = "text.abuse", groupId = "respect-zone-group")
    public void consumeAbuse(String message) {
        log.info("Consumed message from 'text.abuse': {}", message);
        processMessage(message);
    }

    @KafkaListener(topics = "text.clean", groupId = "respect-zone-group")
    public void consumeClean(String message) {
        log.info("Consumed message from 'text.clean': {}", message);
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(message);
            String text = jsonNode.get("text").asText();
            Long speechId = jsonNode.get("speechId").asLong();
            String timestamp = jsonNode.get("timestamp").asText();
            String predLabel = jsonNode.get("pred_label").asText();
            double predScore = jsonNode.get("pred_score").asDouble();

            // scores 맵 생성
            Map<String, Double> scores = new HashMap<>();
            JsonNode scoresNode = jsonNode.get("scores");
            scores.put("성별", scoresNode.get("성별").asDouble());
            scores.put("연령", scoresNode.get("연령").asDouble());
            scores.put("기타혐오", scoresNode.get("기타혐오").asDouble());
            scores.put("악플/욕설", scoresNode.get("악플/욕설").asDouble());
            scores.put("clean", scoresNode.get("clean").asDouble());

            // SentenceType 결정
            SentenceType sentenceType = getSentenceTypeFromLabel(predLabel);

            // 응답 DTO 생성
            SentenceAnalysisResponseDto responseDto = SentenceAnalysisResponseDto.builder()
                    .speechId(speechId)
                    .sentence(text)
                    .sentenceType(sentenceType)
                    .predScore(predScore)
                    .scores(scores)
                    .message("문장 분석이 완료되었습니다.")
                    .build();

            // WebSocket을 통해 클라이언트에게 전송
            ApiResponse<SentenceAnalysisResponseDto> apiResponse = ApiResponse.onSuccess(responseDto);
            messagingTemplate.convertAndSend(
                    "/topic/speech/" + speechId + "/analysis",
                    apiResponse);

            // GOOD_SENTENCE가 아닌 경우에만 DB에 저장
            if (sentenceType != SentenceType.GOOD_SENTENCE) {
                try {
                    // Sentence 저장
                    LocalDateTime sentenceTimestamp = LocalDateTime.parse(timestamp);
                    sentenceCommandService.saveSentence(
                            speechId,
                            text,
                            sentenceType,
                            sentenceTimestamp);
                    log.info("Saved hate sentence to DB: {}", text);
                } catch (Exception e) {
                    log.error("Error saving sentence to DB: {}", e.getMessage());
                }
            }

            // Speech 조회 및 카운트 증가
            Speech speech = speechRepository.findById(speechId)
                    .orElseThrow(() -> new RuntimeException("Speech not found"));
            speech.incrementSentenceCount();

            // GOOD_SENTENCE가 아닌 경우에만 swearCount 증가 및 Sentence 저장
            if (sentenceType != SentenceType.GOOD_SENTENCE) {
                speech.incrementSwearCount();
                try {
                    // Sentence 저장
                    LocalDateTime sentenceTimestamp = LocalDateTime.parse(timestamp);
                    sentenceCommandService.saveSentence(
                            speechId,
                            text,
                            sentenceType,
                            sentenceTimestamp);
                    log.info("Saved hate sentence to DB: {}", text);
                } catch (Exception e) {
                    log.error("Error saving sentence to DB: {}", e.getMessage());
                }
            }

            speechRepository.save(speech);

            log.info("Processed message from Kafka: {}", message);
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private SentenceType getSentenceTypeFromLabel(String label) {
        return switch (label) {
            case "성별" -> SentenceType.GENDER_HATE;
            case "연령" -> SentenceType.AGE_HATE;
            case "기타혐오" -> SentenceType.OTHER_HATE;
            case "악플/욕설" -> SentenceType.SWEAR_EXPRESSION;
            case "clean" -> SentenceType.GOOD_SENTENCE;
            default -> SentenceType.GOOD_SENTENCE;
        };
    }
}
