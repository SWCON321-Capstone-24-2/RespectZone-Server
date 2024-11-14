package khu.dacapstone.respect_zone.web.dto;

import java.util.Map;

import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentenceAnalysisResponseDto {
    private Long speechId;
    private String sentence;
    private SentenceType sentenceType;
    private String message;
    private double predScore;
    private Map<String, Double> scores;
}