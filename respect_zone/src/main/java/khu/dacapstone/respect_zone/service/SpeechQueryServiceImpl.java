package khu.dacapstone.respect_zone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.apiPayload.code.ErrorStatus;
import khu.dacapstone.respect_zone.apiPayload.exception.handler.SpeechHandler;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.repository.SpeechRepository;

@Service
public class SpeechQueryServiceImpl implements SpeechQueryService {

    private final SpeechRepository speechRepository;

    public SpeechQueryServiceImpl(SpeechRepository speechRepository) {
        this.speechRepository = speechRepository;
    }

    // Speech가 존재하는지, deviceId가 일치하는지 확인하는 메서드
    @Override
    public void checkSpeech(Long id, String deviceId) {
        Speech speech = speechRepository.findById(id)
                .orElseThrow(() -> new SpeechHandler(ErrorStatus.SPEECH_NOT_FOUND));

        if (!speech.getDeviceId().equals(deviceId)) {
            throw new SpeechHandler(ErrorStatus.SPEECH_NOT_MATCH_DEVICE_ID);
        }
    }

    // 주어진 deviceId에 해당하는 모든 Speech 조회
    @Override
    public List<Speech> getAllSpeechByDeviceId(String deviceId) {
        return speechRepository.findByDeviceId(deviceId);
    }

    // 주어진 id와 deviceId에 해당하는 Speech가 존재하는지 확인
    @Override
    public void deleteSpeech(Long id, String deviceId) {
        Optional<Speech> speech = speechRepository.findById(id);

        if (!speech.isPresent()) {
            throw new SpeechHandler(ErrorStatus.SPEECH_NOT_FOUND);
        }

        if (!speech.get().getDeviceId().equals(deviceId)) {
            throw new SpeechHandler(ErrorStatus.SPEECH_NOT_MATCH_DEVICE_ID);
        }

        speechRepository.deleteById(id);
    }

    @Override
    public Speech getSpeech(Long id, String deviceId) {
        return speechRepository.findById(id)
                .orElseThrow(() -> new SpeechHandler(ErrorStatus.SPEECH_NOT_FOUND));
    }

}
