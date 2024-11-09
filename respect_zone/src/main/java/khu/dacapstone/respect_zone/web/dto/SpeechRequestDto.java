package khu.dacapstone.respect_zone.web.dto;

import java.time.LocalDateTime;
import lombok.Getter;

public class SpeechRequestDto {
    
    @Getter
    public static class CreateSpeechDto {
        private LocalDateTime timestamp;
    }
    
    @Getter
    public static class SaveSpeechDto {
        private LocalDateTime timestamp;
    }
} 