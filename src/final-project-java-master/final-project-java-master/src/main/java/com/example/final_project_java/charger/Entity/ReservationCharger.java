package com.example.final_project_java.charger.Entity;

import com.example.final_project_java.userapi.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservation_charger")
public class ReservationCharger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chargeNo; // 예약 순서

    @Builder.Default
    private int reservationNo  = (int)(Math.random() * 89999) + 100000; // 예약 번호

    private LocalDateTime rentTime; // 예약 날짜와 시간
    private LocalDateTime endTime;

    private int rentChargePrice; // 가격

    private String stationName; // 충전소명

    private String address; // 충전소 위치

    private String phoneNumber;

    private String name;

    private int time; // 시간 단위

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "email", referencedColumnName = "email"),
            @JoinColumn(name = "login_method", referencedColumnName = "login_method")
    })
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = ALL)
    @JoinColumn(name = "station_id")
    private ChargingStation station; // 충전소 ID

    @PrePersist
    private void calculateEndTime() {
        this.endTime = this.rentTime.plusMinutes(this.time);
    }
}
