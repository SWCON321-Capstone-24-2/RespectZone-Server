package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;

import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;

public interface SentenceCommandService {

    Sentence saveSentence(Long speechId, String text, SentenceType type, LocalDateTime timestamp);

}
