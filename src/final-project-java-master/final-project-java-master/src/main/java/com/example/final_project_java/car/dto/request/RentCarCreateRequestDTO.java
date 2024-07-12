package com.example.final_project_java.car.dto.request;

import com.example.final_project_java.car.entity.RentCar;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RentCarCreateRequestDTO {

    @NotBlank
    private int reservationNo;

    @NotBlank
    private String userId;

    private String userName;

//    @NotBlank
    private int carNo;

    private String carName;

//    private LocalDate rentDate;

    private String rentTime;

//    private LocalDate turninDate;

    private String turninTime;

    private int totalPrice;

    private String phoneNumber;

    private String extra;

    // DTO -> Entity
    public RentCar toEntity(LocalDateTime rentTime, LocalDateTime turninTime) {
        return RentCar.builder()
                .reservationNo(reservationNo)
                .userName(userName)
                .userId(userId)
                .carName(carName)
                .carNo(carNo)
//                .rentDate(rentDate)
                .rentTime(rentTime)
//                .turninDate(turninDate)
                .turninTime(turninTime)
                .totalPrice(totalPrice)
                .phoneNumber(phoneNumber)
                .extra(extra)
                .build();
    }

}
