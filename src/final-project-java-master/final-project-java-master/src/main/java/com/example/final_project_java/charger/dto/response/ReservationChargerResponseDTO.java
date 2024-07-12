package com.example.final_project_java.charger.dto.response;

import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.charger.Entity.ReservationCharger;
import com.example.final_project_java.userapi.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationChargerResponseDTO { // 전기차 충전소 예약

    // 예약 확인 -> 이름, 휴대폰번호, 예약 시간, 가격, 충전소명, 충전소 위치
    private int chargeNo;
    private int reservationNo;
    private String name;
    private String phoneNumber;
    private String email;
    private String stationId;
    private String stationName;
    private LocalDateTime rentTime;
    private int rentChargePrice;
    private String address;
    private int time;
    private LocalDateTime endTime;

    public ReservationChargerResponseDTO(User user, ChargingStation charge, ReservationCharger reservation) {
        this.chargeNo = reservation.getChargeNo();
        this.reservationNo = reservation.getReservationNo();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.stationId = charge.getStationId();
        this.stationName = charge.getStationName();
        this.rentTime = reservation.getRentTime();
        this.rentChargePrice = reservation.getRentChargePrice();
        this.address = charge.getAddress();
        this.time = reservation.getTime();
        this.endTime = reservation.getEndTime();
    }

    public ReservationChargerResponseDTO(ReservationCharger reservation) {
        this.chargeNo = reservation.getChargeNo();
        this.reservationNo = reservation.getReservationNo();
        this.name = reservation.getUser().getName();
        this.phoneNumber = reservation.getUser().getPhoneNumber();
        this.email = reservation.getUser().getEmail();
        this.stationId = reservation.getStation().getStationId();
        this.stationName = reservation.getStationName();
        this.rentTime = reservation.getRentTime();
        this.rentChargePrice = reservation.getRentChargePrice();
        this.address = reservation.getAddress();
        this.time = reservation.getTime();
        this.endTime = reservation.getEndTime();
    }

}
