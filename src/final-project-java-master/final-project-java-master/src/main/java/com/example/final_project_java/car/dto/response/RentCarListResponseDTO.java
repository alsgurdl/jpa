package com.example.final_project_java.car.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentCarListResponseDTO {

    private String error; // 에러 필드
    private List<RentCarDetailResponseDTO> rentList; // 예약 목록
}
