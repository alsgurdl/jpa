package com.example.final_project_java.charger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationListResponseDTO {
    private String error;
    private List<ReservationChargerResponseDTO> ReservedStationList;
}
