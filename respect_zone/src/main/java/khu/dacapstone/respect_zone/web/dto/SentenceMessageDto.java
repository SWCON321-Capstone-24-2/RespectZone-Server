package khu.dacapstone.respect_zone.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SentenceMessageDto {
    private String sentence;
    private LocalDateTime timestamp;
}