package com.example.final_project_java.car.dto.request;

import com.example.final_project_java.car.entity.RentCar;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentCarResModifyRequestDTO {

//    @NotBlank
    private int carNo; // 렌트 pk

    private String rentTime; // 픽업시간

    private String turninTime; // 반납시간

    private String extra; // 비고

    public RentCar toEntity(LocalDateTime rentTime, LocalDateTime turninTime) {
        return RentCar.builder()
                .rentTime(rentTime)
                .turninTime(turninTime)
                .build();
    }

}
