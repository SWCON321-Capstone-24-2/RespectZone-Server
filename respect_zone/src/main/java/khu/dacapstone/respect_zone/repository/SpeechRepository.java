package khu.dacapstone.respect_zone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import khu.dacapstone.respect_zone.domain.Speech;

public interface SpeechRepository extends JpaRepository<Speech, Long> {
    // deviceId + Id에 해당하는 Speech 가져오는 메서드
    Optional<Speech> findByIdAndDeviceId(Long id, String deviceId);

    // sentences를 제외한 Speech 데이터를 가져오는 메서드
    @Query("SELECT new khu.dacapstone.respect_zone.domain.Speech(s.id, s.deviceId, s.createdAt, s.recordingTime, s.burningCount) FROM Speech s WHERE s.deviceId = :deviceId")
    List<Speech> findByDeviceId(@Param("deviceId") String deviceId);
}
