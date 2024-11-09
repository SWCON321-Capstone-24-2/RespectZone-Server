package khu.dacapstone.respect_zone.service;

import java.util.List;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.repository.SentenceRepository;

@Service
public class SentenceQueryServiceImpl implements SentenceQueryService {

    private final SentenceRepository sentenceRepository;

    public SentenceQueryServiceImpl(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    @Override
    public List<Sentence> getSentencesBySpeechId(Long speechId, String deviceId) {
        return sentenceRepository.findBySpeechId(speechId);
    }
}
