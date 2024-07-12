package com.example.final_project_java.charger.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationChargerModifyRequestDTO {

    @NotBlank
    private String chargeNo;

    private LocalDateTime rentTime;

}