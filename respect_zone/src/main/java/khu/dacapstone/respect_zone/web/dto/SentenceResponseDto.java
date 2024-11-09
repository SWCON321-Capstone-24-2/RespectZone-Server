package khu.dacapstone.respect_zone.web.dto;

import java.util.List;

import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SentenceResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSentencesBySpeechIdDto {
        List<Sentence> sentences;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class saveSentenceDto {
        String sentence;
        SentenceType type;
    }
}
