package khu.dacapstone.respect_zone.service;

public interface SentenceAnalysisService {
    void analyzeSentence(String sentence, Long speechId, String timestamp);
}