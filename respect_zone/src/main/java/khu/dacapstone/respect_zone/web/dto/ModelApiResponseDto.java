package khu.dacapstone.respect_zone.web.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModelApiResponseDto {
    private String speechId;
    private String timestamp;
    private String text;
    private String pred_label;
    private double pred_score;
    private Map<String, Double> scores;
}