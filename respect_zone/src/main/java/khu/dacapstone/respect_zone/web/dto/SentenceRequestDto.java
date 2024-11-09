package khu.dacapstone.respect_zone.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SentenceRequestDto {

    @Getter
    public static class getSentencesBySpeechIdDto extends SentenceRequestDto {

    }

    @Getter
    public static class saveSentenceDto extends SentenceRequestDto {
        String sentence;
        LocalDateTime timestamp;
    }
}
