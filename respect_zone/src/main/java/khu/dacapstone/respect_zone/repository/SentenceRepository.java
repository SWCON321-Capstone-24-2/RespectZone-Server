package khu.dacapstone.respect_zone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import khu.dacapstone.respect_zone.domain.Sentence;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findBySpeechId(Long speechId);
}
