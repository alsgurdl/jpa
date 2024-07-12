package com.example.final_project_java.charger.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "charging_station")
public class ChargingStation {

    @Id
    private String stationId; // 충전소 ID

    private String province; // 시
    private String districts; // 군구
    private String address; // 충전소 위치
    private String stationName; // 충전소명
    private String speed; // 충전 속도 (완속 / 급속)
    private String management; // 충전기 회사
    private String chargerType; // 충전기 타입
    private int chargingPrice; // 가격
    private String areaIn; // 이용 시설
    private String available; // 충전 가능 유무 (이용 가능 / 이용자 제한)
    private String latitude; // 위도
    private String longitude; // 경도
    private boolean reservation_possible; // 예약 가능한 충전소를 나타내기 위해 만듦

}
