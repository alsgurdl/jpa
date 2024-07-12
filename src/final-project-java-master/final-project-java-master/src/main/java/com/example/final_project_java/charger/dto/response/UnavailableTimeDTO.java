package com.example.final_project_java.charger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnavailableTimeDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
