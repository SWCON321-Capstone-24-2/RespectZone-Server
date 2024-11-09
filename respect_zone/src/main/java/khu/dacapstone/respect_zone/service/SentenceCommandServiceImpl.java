package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import khu.dacapstone.respect_zone.repository.SentenceRepository;

@Service
public class SentenceCommandServiceImpl implements SentenceCommandService {

    private final SentenceRepository sentenceRepository;

    public SentenceCommandServiceImpl(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    @Override
    public Sentence saveSentence(Speech speech, String text, SentenceType type, LocalDateTime timestamp) {
        Sentence sentence = new Sentence();
        sentence.setSpeech(speech);
        sentence.setText(text);
        sentence.setType(type);
        sentence.setTimestamp(timestamp);
        return sentenceRepository.save(sentence);
    }

}
