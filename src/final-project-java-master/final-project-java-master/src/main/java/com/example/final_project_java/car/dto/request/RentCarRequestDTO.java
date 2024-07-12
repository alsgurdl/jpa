package com.example.final_project_java.car.dto.request;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.entity.RentCar;
import com.example.final_project_java.userapi.entity.User;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentCarRequestDTO {

    // 전기차 예약하기 -> 회원이름, 전화번호, 생일, 차 id, 차 이름, 픽업 날짜, 픽업 시간, 반납 날짜, 반납 시간, 비고

    private String carId; // 차id
//    private String userName;
//    private String phoneNumber;
//    private LocalDate birthday;
//    private String email;
//    private String userId;
    private String carName;
//    private LocalDate rentDate;
    private String rentTime;
//    private LocalDate turninDate;
    private String turninTime;
    private int totalPrice;
    private String extra; // 비고


    public RentCar toEntity (User user, Car car, LocalDateTime rentTime, LocalDateTime turninTime) {
        return RentCar.builder()
                .carId(car.getCarId()) // 있는곳에서 가져올 때
                .userName(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .userId(user.getId())
                .carName(car.getCarName())
                .totalPrice(totalPrice)
//                .rentDate(rentDate) // 새로 유저에게 받아야 할 때
                .rentTime(rentTime)
//                .turninDate(turninDate)
                .turninTime(turninTime)
                .extra(extra)
                .build();
    }

}
