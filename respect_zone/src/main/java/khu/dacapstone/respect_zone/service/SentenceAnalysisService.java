package khu.dacapstone.respect_zone.service;

import khu.dacapstone.respect_zone.domain.enums.SentenceType;

public interface SentenceAnalysisService {
    SentenceType analyzeSentence(String sentence);
}