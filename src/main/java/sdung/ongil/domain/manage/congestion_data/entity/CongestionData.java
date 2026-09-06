package sdung.ongil.domain.manage.congestion_data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sdung.ongil.domain.manage.stations.entity.ManageStations;

import java.time.LocalDateTime;

@Entity
@Table(name = "congestion_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CongestionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private ManageStations station;

    @Column(nullable = false)
    private LocalDateTime timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CongestionLevel level;

    @Column(nullable = false)
    private Double averageIntervalMinutes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public CongestionData(ManageStations stations, LocalDateTime timeSlot, CongestionLevel level, Double averageIntervalMinutes) {
        this.station = station;
        this.timeSlot = timeSlot;
        this.level= level;
        this.averageIntervalMinutes = averageIntervalMinutes;
    }
}
