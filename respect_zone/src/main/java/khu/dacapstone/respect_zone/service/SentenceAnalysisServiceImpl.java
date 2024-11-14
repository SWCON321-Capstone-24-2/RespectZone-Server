package khu.dacapstone.respect_zone.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SentenceAnalysisServiceImpl implements SentenceAnalysisService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SentenceAnalysisServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void analyzeSentence(String sentence, Long speechId, String timestamp) {
        try {
            // JSON 메시지 생성
            Map<String, Object> message = new HashMap<>();
            message.put("text", sentence);
            message.put("speechId", speechId);
            message.put("timestamp", timestamp);

            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("text.all", jsonMessage);
            log.info("Sent message to Kafka topic 'text.all': {}", jsonMessage);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}