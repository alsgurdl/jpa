package com.example.final_project_java.charger.dto.response;

import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChargerListResponseDTO { // 전국 전기차 충전소 목록

    private int count; // 총 충전소 수
    private String error; // 에러 발생 시 에러 메세지를 담을 필드
    private List<ChargerDetailResponseDTO> chargers; // 충전소 목록들

}
