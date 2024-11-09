package khu.dacapstone.respect_zone.service;

import java.time.LocalDateTime;

import khu.dacapstone.respect_zone.domain.Speech;

public interface SpeechCommandService {

    // Speech 객체 생성 후 DB에 저장
    Speech createSpeech(String deviceId, LocalDateTime timestamp);

    // Speech 객체 저장
    Speech saveSpeech(Long id, String deviceId, LocalDateTime timestamp);
}
