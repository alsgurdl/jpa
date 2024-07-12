package com.example.final_project_java.charger.dto.response;

import com.example.final_project_java.charger.Entity.ChargingStation;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargerDetailResponseDTO {

    private String stationId; // 충전소 id
    private String stationName; // 충전소명
    private String province; // 시 & 도
    private String districts; // 시 & 군 & 구
    private String address; // 충전소 위치
    private String speed; // 충전 스피드 (급속 / 완속)
    private String chargerType; // 충전기 타입
    private String management; // 충전기 회사
    private int chargingPrice; // 가격
    private String areaIn; // 이용 시설
    private String available; // 이용자 제한 / 이용 가능
    private String latitude; // 위도
    private String longitude; // 경도

    private boolean reservationPossible; // 예약 가능 여부 (플앤고 충전소만)

    // 엔터티 -> DTO
    public ChargerDetailResponseDTO(ChargingStation station) {
        this.stationId = station.getStationId();
        this.stationName = station.getStationName();
        this.province = station.getProvince();
        this.districts = station.getDistricts();
        this.address = station.getAddress();
        this.speed = station.getSpeed();
        this.chargerType = station.getChargerType();
        this.management = station.getManagement();
        this.chargingPrice = station.getChargingPrice();
        this.areaIn = station.getAreaIn();
        this.available = station.getAvailable();
        this.latitude = station.getLatitude();
        this.longitude = station.getLongitude();

        this.reservationPossible = station.isReservation_possible();
    }

}