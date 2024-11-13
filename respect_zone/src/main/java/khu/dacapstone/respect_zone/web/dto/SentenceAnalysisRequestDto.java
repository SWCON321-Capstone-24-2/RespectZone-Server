package khu.dacapstone.respect_zone.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SentenceAnalysisRequestDto {
    private Long speechId;
    private String deviceId;
    private String sentence;
}