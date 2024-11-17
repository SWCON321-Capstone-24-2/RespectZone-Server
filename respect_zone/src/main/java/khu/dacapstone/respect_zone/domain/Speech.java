package khu.dacapstone.respect_zone.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Speech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "recording_time")
    private LocalTime recordingTime;

    @PositiveOrZero
    @Column(name = "burning_count", columnDefinition = "bigint default 0")
    private Long burningCount;

    @OneToMany(mappedBy = "speech", orphanRemoval = true)
    private List<Sentence> sentences;

    @Column(name = "sentence_count", columnDefinition = "integer default 0")
    private Integer sentenceCount = 0;

    @Column(name = "swear_count", columnDefinition = "integer default 0")
    private Integer swearCount = 0;

    public Speech(Long id, String deviceId, LocalDateTime createdAt, LocalTime recordingTime, Long burningCount,
            Integer sentenceCount, Integer swearCount) {
        this.id = id;
        this.deviceId = deviceId;
        this.createdAt = createdAt;
        this.recordingTime = recordingTime;
        this.burningCount = burningCount;
        this.sentenceCount = sentenceCount;
        this.swearCount = swearCount;
    }

    public void incrementSentenceCount() {
        this.sentenceCount = (this.sentenceCount == null) ? 1 : this.sentenceCount + 1;
    }

    public void incrementSwearCount() {
        this.swearCount = (this.swearCount == null) ? 1 : this.swearCount + 1;
    }

}
