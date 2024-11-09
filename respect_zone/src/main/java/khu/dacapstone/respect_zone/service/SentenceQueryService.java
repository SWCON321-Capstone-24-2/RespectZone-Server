package khu.dacapstone.respect_zone.service;

import java.util.List;

import khu.dacapstone.respect_zone.domain.Sentence;

public interface SentenceQueryService {
    List<Sentence> getSentencesBySpeechId(Long speechId, String deviceId);

}
