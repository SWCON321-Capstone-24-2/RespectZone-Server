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

    public Speech(Long id, String deviceId, LocalDateTime createdAt, LocalTime recordingTime, Long burningCount) {
        this.id = id;
        this.deviceId = deviceId;
        this.createdAt = createdAt;
        this.recordingTime = recordingTime;
        this.burningCount = burningCount;
    }

}