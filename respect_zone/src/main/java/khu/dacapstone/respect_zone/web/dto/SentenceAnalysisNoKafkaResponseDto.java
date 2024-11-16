package khu.dacapstone.respect_zone.web.dto;

import java.util.Arrays;
import java.util.Map;

import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SentenceAnalysisNoKafkaResponseDto {
    private String pred_label;
    private double pred_score;
    private Map<String, Double> scores;

    public SentenceAnalysisResponseDto toSentenceAnalysisResponseDto(Long speechId, String sentence) {
        return SentenceAnalysisResponseDto.builder()
                .speechId(speechId)
                .sentence(sentence)
                .sentenceType(convertToSentenceType(pred_label))
                .message("문장 분석이 완료되었습니다.")
                .predScore(pred_score)
                .scores(scores)
                .build();
    }

    // pred_label(value)을 SentenceType(name)으로 변환
    private SentenceType convertToSentenceType(String predLabel) {
        return Arrays.stream(SentenceType.values())
                .filter(type -> type.getValue().equals(predLabel))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid pred_label: " + predLabel));
    }
}