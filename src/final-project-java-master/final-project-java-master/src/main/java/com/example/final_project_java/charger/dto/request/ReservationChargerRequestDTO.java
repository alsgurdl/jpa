package com.example.final_project_java.charger.dto.request;

import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.charger.Entity.ReservationCharger;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.entity.UserId;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationChargerRequestDTO {

    // 충전소 예약하기 -> 회원이름, 휴대폰번호, 충전소 ID, 시간

    private String chargeId;
    private String name;
    private String phoneNumber;
    private String email;
    private String stationName;
    private String address;
    private String speed;
    private int price;
    private int selectedValue;
    private ZonedDateTime startDate;

    public ReservationCharger toEntity(User user, ChargingStation charge) {
        return ReservationCharger.builder()
                .user(user)
                .station(charge)
                .address(this.address)
                .name(user.getName())
                .phoneNumber(this.phoneNumber)
                .rentChargePrice(this.price)
                .rentTime(this.startDate.toLocalDateTime())
                .time(this.selectedValue)
                .stationName(this.stationName)
                .build();
    }

    public void setStartDate(String startDate) {
        this.startDate = ZonedDateTime.parse(startDate).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    }
}

