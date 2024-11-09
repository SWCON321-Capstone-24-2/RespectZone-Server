package khu.dacapstone.respect_zone.service;

import java.util.List;

import khu.dacapstone.respect_zone.domain.Speech;

public interface SpeechQueryService {
    // Speech가 존재하는지, deviceId가 일치하는지 확인하는 메서드
    void checkSpeech(Long id, String deviceId);

    // 주어진 deviceId에 해당하는 모든 Speech 조회
    List<Speech> getAllSpeechByDeviceId(String deviceId);

    // 주어진 id와 deviceId에 해당하는 Speech가 존재하는지 확인
    void deleteSpeech(Long id, String deviceId);

    // Speech 조회
    Speech getSpeech(Long id, String deviceId);

}
