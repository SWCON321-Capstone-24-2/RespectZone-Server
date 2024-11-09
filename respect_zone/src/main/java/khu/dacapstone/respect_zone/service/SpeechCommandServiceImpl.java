package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.repository.SpeechRepository;

@Service
public class SpeechCommandServiceImpl implements SpeechCommandService {

    private final SpeechQueryService speechQueryService;

    private final SpeechRepository speechRepository;

    public SpeechCommandServiceImpl(SpeechQueryService speechQueryService, SpeechRepository speechRepository) {
        this.speechQueryService = speechQueryService;
        this.speechRepository = speechRepository;
    }

    @Override
    public Speech createSpeech(String deviceId, LocalDateTime timestamp) {
        Speech speech = new Speech();
        speech.setDeviceId(deviceId);
        speech.setCreatedAt(timestamp);
        speech.setBurningCount(0L);
        return speechRepository.save(speech);
    }

    @Override
    public Speech saveSpeech(Long id, String deviceId, LocalDateTime timestamp) {
        // Speech 존재 여부와 deviceId 일치 여부 확인
        speechQueryService.checkSpeech(id, deviceId);

        // Speech 객체 조회
        Speech speech = speechRepository.findById(id).get();

        // 녹음 시작 시간과 종료 시간의 차이를 계산하여 녹음 시간 설정
        LocalDateTime startTime = speech.getCreatedAt();
        LocalDateTime endTime = timestamp;
        long secondsBetween = ChronoUnit.SECONDS.between(startTime, endTime);
        int hours = (int) (secondsBetween / 3600);
        int minutes = (int) ((secondsBetween % 3600) / 60);
        int seconds = (int) (secondsBetween % 60);
        speech.setRecordingTime(LocalTime.of(hours, minutes, seconds));

        // 업데이트된 Speech 객체 저장
        return speechRepository.save(speech);
    }
}
