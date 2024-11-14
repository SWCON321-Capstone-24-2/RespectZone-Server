package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import khu.dacapstone.respect_zone.repository.SentenceRepository;
import khu.dacapstone.respect_zone.repository.SpeechRepository;

@Service
public class SentenceCommandServiceImpl implements SentenceCommandService {

    private final SentenceRepository sentenceRepository;
    private final SpeechRepository speechRepository;

    public SentenceCommandServiceImpl(SentenceRepository sentenceRepository, SpeechRepository speechRepository) {
        this.sentenceRepository = sentenceRepository;
        this.speechRepository = speechRepository;
    }

    @Override
    public Sentence saveSentence(Long speechId, String text, SentenceType type, LocalDateTime timestamp) {
        Sentence sentence = new Sentence();
        sentence.setSpeech(speechRepository.getReferenceById(speechId));
        sentence.setText(text);
        sentence.setType(type);
        sentence.setTimestamp(timestamp);
        return sentenceRepository.save(sentence);
    }

}
