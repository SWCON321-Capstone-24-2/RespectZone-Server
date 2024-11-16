package khu.dacapstone.respect_zone.web.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import khu.dacapstone.respect_zone.web.dto.deserializer.CustomLocalDateTimeDeserializer;
import lombok.Getter;

@Getter
public class SentenceRequestDto {

    @Getter
    public static class getSentencesBySpeechIdDto extends SentenceRequestDto {

    }

    @Getter
    public static class saveSentenceDto extends SentenceRequestDto {
        private String sentence;

        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime timestamp;
    }
}
